package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dao.EventDao;
import com.tutorial.booking.system.dao.UserDao;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
public class FrontController {

    private UserDao user;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

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
        System.out.println(user.getUserId());
        model.addAttribute("event", new EventDao());
        return "addEvent";
    }

    @PostMapping("/event/add")
    public String addEvent(@ModelAttribute EventDao event, BindingResult bindingResult, Model model) throws ParseException {
        //Save the event
        //System.out.println(event);
        System.out.println(event.toString());


        eventService.add(event);

        //Redirect to the homepage
        return "index";
    }

    @GetMapping("/error")
    public String error(){
        return "login";
    }

    public void makeUserDao(Authentication authentication){
        System.out.println(authentication.getName());
        user = userService.getUserByUserName(authentication.getName());
    }
}
