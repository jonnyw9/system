package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.NotificationService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications/")
public class NotificationController {

    private UserDto user;

    private static final String returnPrefix = "notification/";

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;

    @GetMapping("{id}")
    public String viewUserNotifications(@PathVariable(name = "id") int id, Model model, Authentication authentication){

        user = userService.makeUserDto(authentication);

        notificationService.getUserNotifications(id);

        return returnPrefix + "viewNotifications";
    }
}
