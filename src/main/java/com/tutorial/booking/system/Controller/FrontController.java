/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Constraint.UserValidation;
import com.tutorial.booking.system.Repository.CalendarRepository;
import com.tutorial.booking.system.Service.*;

import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>A controller for the front of the system aswell as other mappings which don't fall into any easy categories</p>
 * @author Jonathan Watt
 */
@Controller
@RequestMapping("/")
public class FrontController {

    private UserDto user;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserValidation userValidation;

    /**
     * <p>Displays the landing page of the web app.</p>
     * @return - the index page.
     */
    @GetMapping("/")
    public String index(){
        return "index";
    }

    /**
     * <p>Method for the homepage once the user is logged in.</p>
     * @param authentication - The authenticated user.
     * @param model - the model to add attributes to.
     * @return - The homepage template.
     */
    @RequestMapping(value = "home", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(Authentication authentication, Model model){
        //Add the pertinent information for the page. See the home.html for what needs to be added.
        user = userService.makeUserDto(authentication);

        int userId = user.getUserId();

        model.addAttribute("user", user);

        String url = "/api/event/getall/" + String.valueOf(userId);

        model.addAttribute("url", url);

        String name = "";

        model.addAttribute("name", name);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        return "home";
    }

    /**
     * <p>Handles the mapping for the search.</p>
     * @param name - The name to search
     * @param model - the model to add attributes to.
     * @param authentication - The authenticated user.
     * @return - The view to render.
     */
    @GetMapping("search")
    public String searchStaff(@RequestParam(value = "name") Optional<String> name,
                              //@RequestParam(value = "page") Optional<Integer> page,
                              Model model,
                              Authentication authentication){

        user =  userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        //Apply the searched name to the model
        model.addAttribute("name", name.orElse(""));
        try{
            //try getting a list from the database with the name given
            String nameReplace;

            nameReplace = name.orElse("_").replace("+", "");

            model.addAttribute("searchResults",
                    userService.listUserByName(nameReplace));

        }catch (Exception e){
            //If it fails search for all names.
            model.addAttribute("searchResults",
                    userService.listUserByName(name.orElse("_")));
        }
        return "searchStaff";
    }

    /**
     * <p>Handles the getting of attributes for the register mapping.</p>
     * @param model - the model to add attributes to.
     * @return - The register view.
     */
    @GetMapping("register")
    public String register(Model model){
        if(!model.containsAttribute("user")){
            UserDto userDto = new UserDto();
            model.addAttribute("user", userDto);
        }
        return "register";
    }

    /**
     * <p>Handles the post mapping for registration.</p>
     * @param user - The userDto of the user to sign up.
     * @param bindingResult - The bindingResult for any potential errors.
     * @param redirectAttributes - The redirectAttributes.
     * @return - redirect to the register form or the login page.
     */
    @PostMapping("register")
    public String register(@ModelAttribute("user") @Valid UserDto user, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        //Validate the user
        bindingResult = userValidation.validate(user, bindingResult);

        //Validate the password
        if(!user.getPassword().equals(user.getConfirmPassword())){
            bindingResult.rejectValue("password", "password.mismatch",
                    "The passwords do not match.");
        }

        //Check for errors and if there are redirect back to form
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",
                    bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }
        //Save the user and send a notification
        User user1 = userService.saveNewUser(user);

        if(user1 != null){
            notificationService.accountCreated(user1);
        }
        //Send to login page.
        return "redirect:/login?registered";
    }

    /**
     * <p>Mapping to log a user out of a system.</p>
     * @param request - the request
     * @param response - the response
     * @return - The login page.
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        //Get the context of the authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //If there is a context (Someone is logged in)
        if(authentication != null){
            //Log the user out of the context.
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            user = null;
        }
        return "redirect:/login?logout";
    }

    /**
     * <p>Generic method to handle login. Spring security will handle the authentication.
     * This method just points to the form.</p>
     * @return - The login form
     */
    @GetMapping("login")
    public String login(){
        return "login";
    }


    /**
     * <p>Mapping to handle errors.</p>
     * @return - The error page.
     */
    @GetMapping("error")
    public String error(){
        return "error";
    }

    /**
     * <p>Gets the calendar for a staff member.</p>
     * @param id - The id of the staff member
     * @param model - The model to add attributes to.
     * @param authentication - The authenticated user
     * @return - The staff calendar view.
     */
    @GetMapping("staff/times/{id}")
    private String getStaffTime(@PathVariable("id") int id, Model model,
                                Authentication authentication){

        user =  userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        User user = userService.getUserById(id);

        model.addAttribute("staffName", user.getFirstName() + " " + user.getLastName());
        //Gets the start day of the staff member
        String dayStart =
                Objects.requireNonNull(calendarRepository.findById(user.getCalendarId().
                        getCalendarId()).orElse(null)).getDayStartTime().toLocalTime().toString();
        model.addAttribute("dayStart", dayStart);

        //Gets the end day of the staff member
        String dayEnd =
                Objects.requireNonNull(calendarRepository.findById(user.getCalendarId()
                        .getCalendarId()).orElse(null)).getDayEndTime().toLocalTime().toString();
        model.addAttribute("dayEnd", dayEnd);
        model.addAttribute("staff", user);
        //Adds the url for the API to allow fullCalendar to get it.
        String url = "/api/event/getall/" + String.valueOf(id);
        model.addAttribute("url", url);

        return "staffCalendar";
    }


}
