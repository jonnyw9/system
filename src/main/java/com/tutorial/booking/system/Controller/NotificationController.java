/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.NotificationService;
import com.tutorial.booking.system.Service.NotificationServiceImpl;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.Service.UserServiceImpl;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p>A controller for the notification class.</p>
 * @author Jonathan Watt
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private UserDto user;

    private static final String returnPrefix = "notification/";

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    /**
     * <p>A method which displays all the users notifications.</p>
     * @param model - The model to apply the attributes to.
     * @param authentication - The authentication of the user
     * @return - The view notifications view.
     */
    @RequestMapping(value={"", "/"}, method = RequestMethod.GET)
    public String viewUserNotifications(Model model, Authentication authentication){

        user = userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        model.addAttribute("notifications", notificationService.getUserNotifications(user.getUserId()));

        return returnPrefix + "viewNotifications";
    }

    /**
     * <p>Method to view and individual notification.</p>
     * @param id- The id of the notification.
     * @param model- the model to apply attributes to.
     * @param authentication - The authentication of the user
     * @return - The view notification model.
     */
    @GetMapping("/view/{id}")
    public String viewNotification(@PathVariable(name = "id") int id, Model model, Authentication authentication){
        user = userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        Notification notification = notificationService.getNotificationById(id);

        //Check if the notification is tied to the user.
        if(notification.getUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourNotif";
        }

        notification.setSeen(true);

        notificationService.saveNotification(notification);

        model.addAttribute("notification", notification);

        return returnPrefix + "viewNotification";
    }

    /**
     * <p>Reads all of a users notifications</p>
     * @param authentication - The authenticate user in the system.
     * @return - a redirect to the homepage.
     */
    @GetMapping("/read/all")
    public String readAllNotifications(Authentication authentication){

        userService.makeUserDto(authentication);

        notificationService.readAllUnreadNotifications(user);

        return "redirect:/home";
    }
}
