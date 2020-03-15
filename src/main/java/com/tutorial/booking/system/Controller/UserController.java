package com.tutorial.booking.system.Controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tutorial.booking.system.Constraint.PasswordValidation;
import com.tutorial.booking.system.Constraint.UserValidation;
import com.tutorial.booking.system.Repository.PasswordRepository;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.PasswordDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/user/")
public class UserController {

    private final String userTemplatePrefix = "user/";

    private final String editPrefix = "edit/";

    private UserDto user;

    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserValidation userValidation;

    @Autowired
    PasswordValidation passwordValidation;

    @GetMapping("view")
    public String viewAccount(Authentication authentication, Model model){
        user = userService.makeUserDto(authentication);
        System.out.println(user.toString());

        model.addAttribute("user", user);

        return userTemplatePrefix + "viewUser";
    }

    @GetMapping(editPrefix + "delete")
    public String deleteAccount(){

        userService.deleteUserAccount(user.getUserId());

        return "redirect:/?deleted";
    }

    @GetMapping(editPrefix + "{editRequest}")
    public String editDetails(@PathVariable String editRequest, Model model){
        if(!model.containsAttribute("user")){
            model.addAttribute("user", user);
        }

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
                default:
                    return "redirect:/user/view?badEditRequest";
            }
        }
    }

    @PostMapping(editPrefix + "{editRequest}")
    public String editDetails(@PathVariable String editRequest, @ModelAttribute(name = "user") UserDto userDto,
                              RedirectAttributes redirectAttributes,
                              BindingResult bindingResult){
        switch(editRequest){
            case "name":
                bindingResult = userValidation.validateName(userDto.getFirstName(),
                        userDto.getLastName(), bindingResult);
                break;
            case "email":
                bindingResult = userValidation.validateEmail(userDto.getEmail(), bindingResult);
                break;
            default:
                return "redirect:/user/view?badEditRequest";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",
                    bindingResult);
            redirectAttributes.addFlashAttribute("user", userDto);
            return "redirect:/user/edit/" + editRequest;
        }

        userService.updateDetails(userDto, editRequest);

        if(editRequest.equals("email")){
            return "redirect:/login?success";
        }

        return "redirect:/user/view?updated";
    }

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
