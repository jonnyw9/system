package com.tutorial.booking.system.Controller;


import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
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
@RequestMapping("/event/")
public class EventController {

    private UserDto user;

    private final String userTemplatePrefix = "event/";

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @GetMapping("add/{id}")
    public String addEvent(@PathVariable("id") int id,Authentication authentication, Model model){
        user =  userService.makeUserDto(authentication);
        model.addAttribute("user", user);

        User recipient =  userService.getUserById(id);

        model.addAttribute("recipient", recipient);

        System.out.println(user.getUserId());
        model.addAttribute("event", new EventDto());
        return userTemplatePrefix + "addEvent";
    }



    @PostMapping("add")
    public String addEvent(@ModelAttribute EventDto event, BindingResult bindingResult, Model model) throws ParseException {
        //Save the event
        eventService.add(event);
        //Redirect to the homepage
        return "redirect:/home";
    }

    @GetMapping("view/{id}")
    public String viewEvent(@PathVariable("id") int id, Model model){

        model.addAttribute("event", eventService.getByEventId(id));

        return userTemplatePrefix + "viewEvent";
    }


    @GetMapping("edit/{id}")
    public String editEvent(@PathVariable("id") int id, Model model){

        Event event = eventService.getByEventId(id);

        EventDto eventDto = new EventDto(event);

        eventDto.setEventStart(eventService.changeTimestapToString(event.getEventStart()));

        eventDto.setEventEnd(eventService.changeTimestapToString(event.getEventEnd()));

        System.out.println(eventDto.toString());

        model.addAttribute("event", eventDto);

        return userTemplatePrefix + "editEvent";
    }

    @PostMapping("edit")
    public String editEvent(@ModelAttribute EventDto event, BindingResult bindingResult, Model model){
        System.out.println(event.toString());
        eventService.updateEvent(event);
        return "redirect:/home";
    }

    @GetMapping("cancel/{id}")
    public String deleteEvent(@PathVariable("id") int id, Model model){

        eventService.cancelEvent(id);

        return "redirect:/home";
    }

}
