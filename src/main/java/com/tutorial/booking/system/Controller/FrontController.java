package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/")
public class FrontController {

    private UserDto user;

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @GetMapping("/")
    public String home(Model model){
        return "index";
    }

    @GetMapping("admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("home")
    public String student(Authentication authentication, Model model){
        user = userService.makeUserDto(authentication);

        int userId = user.getUserId();

        List<Event> events = eventService.getEventsForUser(userId);

        model.addAttribute("events", events);
        model.addAttribute("user", user);

        return "home";
    }

    @GetMapping("register")
    public String register(Model model){

        UserDto userDto = new UserDto();

        model.addAttribute("user", userDto);

        return "register";
    }

    @PostMapping("register")
    public String register(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult){

        User existing = userService.getUserByEmail(userDto.getEmail());
        if(existing != null){
            bindingResult.rejectValue("email", null,
                    "There is already an account registered to that email address!");
        }

        if(bindingResult.hasErrors()){
            return "register";
        }

        userService.save(userDto);

        return "redirect:/login?registered";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            user = null;
        }
        return "redirect:/login?logout";
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }


    @GetMapping("error")
    public String error(){
        return "login";
    }


}
