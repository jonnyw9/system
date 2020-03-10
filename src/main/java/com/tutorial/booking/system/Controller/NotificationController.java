package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.NotificationService;
import com.tutorial.booking.system.Service.UserService;
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

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private UserDto user;

    private static final String returnPrefix = "notification/";

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value={"", "/"}, method = RequestMethod.GET)
    public String viewUserNotifications(Model model, Authentication authentication){

        user = userService.makeUserDto(authentication);

        model.addAttribute("notifications", notificationService.getUserNotifications(user.getUserId()));

        return returnPrefix + "viewNotifications";
    }

    @GetMapping("/view/{id}")
    public String viewNotification(@PathVariable(name = "id") int id, Model model, Authentication authentication){
        user = userService.makeUserDto(authentication);

        Notification notification = notificationService.getNotificationById(id);

        if(notification.getUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourNotif";
        }

        notification.setSeen(true);

        notificationService.saveNotification(notification);

        model.addAttribute("notification", notification);

        return returnPrefix + "viewNotification";
    }
}
