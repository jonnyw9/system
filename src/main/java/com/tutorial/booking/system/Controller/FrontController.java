package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dao.UserDao;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class FrontController {

    private UserDao user;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model){
        return "index";
    }

    @RequestMapping("/staff")
    public String staff(){
        return "staff";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/student")
    public String student(Authentication authentication, Model model){
        makeUserDao(authentication);
        model.addAttribute("user", user);
        return "student";
    }

    @GetMapping("/event/add")
    public String addEvent(Authentication authentication, Model model){
        makeUserDao(authentication);
        model.addAttribute("user", user);
        model.addAttribute("event", new Event());
        return "addEvent";
    }

    @GetMapping("/error")
    public String error(){
        return "login";
    }

    public void makeUserDao(Authentication authentication){
        user = userService.getUserByUserName(authentication.getName());
    }
}
