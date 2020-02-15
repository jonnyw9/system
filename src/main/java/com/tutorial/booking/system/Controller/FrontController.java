package com.tutorial.booking.system.Controller;

import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Password;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@Controller
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

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/home")
    public String student(Authentication authentication, Model model){
        makeUserDao(authentication);
        int userId = user.getUserId();

        List<Event> events = eventService.getEventsForUser(userId);

        model.addAttribute("events", events);
        model.addAttribute("user", user);

        return "home";
    }

    @GetMapping("/register")
    public String register(Model model){

        UserDto userDto = new UserDto();

        model.addAttribute("user", userDto);

        return "register";
    }

    @PostMapping("/register")
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

        return "index";
    }

    @GetMapping("/event/add")
    public String addEvent(Authentication authentication, Model model){
        makeUserDao(authentication);
        model.addAttribute("user", user);
        System.out.println(user.getUserId());
        model.addAttribute("event", new EventDto());
        return "addEvent";
    }

    @PostMapping("/event/add")
    public String addEvent(@ModelAttribute EventDto event, BindingResult bindingResult, Model model) throws ParseException {
        //Save the event
        eventService.add(event);
        //Redirect to the homepage
        return "index";
    }

    @GetMapping("/error")
    public String error(){
        return "login";
    }

    @GetMapping("/event/view/{id}")
    public String viewEvent(@PathVariable("id") int id, Model model){

        model.addAttribute("event", eventService.getByEventId(id));

        return "viewEvent";
    }


    @GetMapping("/event/edit/{id}")
    public String editEvent(@PathVariable("id") int id, Model model){

        Event event = eventService.getByEventId(id);

        EventDto eventDto = new EventDto(event);

        eventDto.setEventStart(eventService.changeTimestapToString(event.getEventStart()));

        eventDto.setEventEnd(eventService.changeTimestapToString(event.getEventEnd()));

        System.out.println(eventDto.toString());

        model.addAttribute("event", eventDto);

        return "editEvent";
    }

    @PostMapping("/event/edit")
    public String editEvent(@ModelAttribute EventDto event, BindingResult bindingResult, Model model){
        System.out.println(event.toString());
        eventService.updateEvent(event);

        return "index";
    }

    @GetMapping("/event/cancel/{id}")
    public String deleteEvent(@PathVariable("id") int id, Model model){

        eventService.cancelEvent(id);

        return "index";
    }



    public void makeUserDao(Authentication authentication){
        System.out.println(authentication.getName());
        user = userService.getUserByUserName(authentication.getName());
    }
}
