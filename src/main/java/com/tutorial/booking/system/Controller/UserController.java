/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Constraint.PasswordValidation;
import com.tutorial.booking.system.Constraint.UserValidation;
import com.tutorial.booking.system.Repository.CalendarRepository;
import com.tutorial.booking.system.Repository.PasswordRepository;
import com.tutorial.booking.system.Repository.StaffRepository;
import com.tutorial.booking.system.Service.NotificationService;
import com.tutorial.booking.system.Service.UserServiceImpl;
import com.tutorial.booking.system.dto.PasswordDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


/**
 * <p>A controller for the user class.</p>
 * @author Jonathan Watt
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    private final String userTemplatePrefix = "user/";

    private final String editPrefix = "edit/";

    private UserDto user;

    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserValidation userValidation;

    @Autowired
    PasswordValidation passwordValidation;

    @Autowired
    NotificationService notificationService;

    /**
     * <p>Views thew account of a given user.</p>
     * @param authentication - The authenticated user in the system.
     * @param model - the model to add attributes to.
     * @return - the view user account view.
     */
    @GetMapping("view")
    public String viewAccount(Authentication authentication, Model model){
        user = userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        return userTemplatePrefix + "viewUser";
    }

    /**
     * <p>Method to delete a users account.</p>
     * @return - The landing page of the web app.
     */
    @GetMapping(editPrefix + "delete")
    public String deleteAccount(){

        userService.deleteUserAccount(user.getUserId());

        return "redirect:/?deleted";
    }

    /**
     * <p>General method to handle the editing of different attributes of a users account.</p>
     * @param editRequest - The attribute to edit.
     * @param model - The model to add attributes to.
     * @param authentication - The authenticated user.
     * @return - The form for the editing of the attribute or the view page.
     */
    @GetMapping(editPrefix + "{editRequest}")
    public String editDetails(@PathVariable String editRequest, Model model,
                              Authentication authentication){
        user = userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        if(!model.containsAttribute("user")){
            model.addAttribute("user", user);
        }

        //Return the page to go to depending on the edit request
        if(editRequest == null){
            return "redirect:/user/view?badEditRequest";
        }else{
            switch (editRequest) {
                case "name":
                    return userTemplatePrefix + "editName";
                case "email":
                    return userTemplatePrefix + "editEmail";
                case "password":
                    if(!model.containsAttribute("passwordDto")){
                        model.addAttribute("passwordDto", new PasswordDto());
                    }
                    return userTemplatePrefix + "editPassword";
                case "room":
                    return userTemplatePrefix + "editRoom";
                case "times":
                    return userTemplatePrefix + "editTimes";
                default:
                    return "redirect:/user/view?badEditRequest";
            }
        }
    }

    /**
     * <p>Post mapping to handle certain edit requests for a user.</p>
     * @param editRequest - The edit request.
     * @param userDto - the userdto
     * @param redirectAttributes - The redirectAttributes if needed
     * @param bindingResult - The bindingResult to add errors too.
     * @return - See code for further details.
     */
    @PostMapping(editPrefix + "{editRequest}")
    public String editDetails(@PathVariable String editRequest, @ModelAttribute(name = "user") UserDto userDto,
                              RedirectAttributes redirectAttributes,
                              BindingResult bindingResult){
        //switch based on the edit request and handle things accordingly
        User userModel;
        switch(editRequest){
            case "name":
                bindingResult = userValidation.validateName(userDto.getFirstName(),
                        userDto.getLastName(), bindingResult);
                break;
            case "email":
                bindingResult = userValidation.validateEmail(userDto.getEmail(), bindingResult);
                break;
            case "room":
                userModel = userService.getUserById(userDto.getUserId());
                if(userModel.getStaffId() != null){
                    bindingResult = userValidation.validateRoom(userDto.getRoom(), bindingResult);
                }else{
                    //If user is not staff redirect back to view user.
                    return "redirect:/user/view?badEditRequest";
                }
                break;
            case "times":
                userModel = userService.getUserById(userDto.getUserId());
                if(userModel.getStaffId() != null){
                    bindingResult = userValidation.validateTimes(userDto.getStartTime(), userDto.getEndTime(), bindingResult);
                }else{
                    //If user is not staff redirect back to view user.
                    return "redirect:/user/view?badEditRequest";
                }
                bindingResult = userValidation.validateTimes(userDto.getStartTime(), userDto.getEndTime(), bindingResult);
                break;
            default:
                //If the edit request does not exists redirect back to view user.
                return "redirect:/user/view?badEditRequest";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",
                    bindingResult);
            redirectAttributes.addFlashAttribute("user", userDto);
            //If there are errors redirect to the edit form
            return "redirect:/user/edit/" + editRequest;
        }

        //Update the details
        userService.updateDetails(userDto, editRequest);

        //If the user changed their email address send them to the login page.
        if(editRequest.equals("email")){
            return "redirect:/login?success";
        }
        //Redirect to the view page.
        return "redirect:/user/view?updated";
    }

    /**
     * <p>Method to handle the post of editing a password.</p>
     * @param passwordDto - The passwordDTO
     * @param bindingResult - The bindingResult to add errors too.
     * @param redirectAttributes - The redirectAttributes if needed
     * @param authentication - The authenticated user.
     * @return - The login page or the form.
     */
    @PostMapping(editPrefix + "password")
    public String editPassword(@ModelAttribute(name = "passwordDto") @Valid PasswordDto passwordDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication){

        user = userService.makeUserDto(authentication);

        bindingResult = passwordValidation.validate(passwordDto, bindingResult, user);


        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordDto", passwordDto);
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordDto",
                    bindingResult);
            return "redirect:/user/edit/password";
        }
        user.setPassword(passwordDto.getPassword());
        user.setConfirmPassword(passwordDto.getConfirmPassword());

        userService.updateDetails(user, "password");

        return "redirect:/login?success";
    }

}
