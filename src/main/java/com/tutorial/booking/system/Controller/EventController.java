package com.tutorial.booking.system.Controller;


import com.tutorial.booking.system.Constraint.EventValidation;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/event/")
public class EventController {

    private UserDto user;

    private final String userTemplatePrefix = "event/";

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    EventValidation eventValidation;

    @RequestMapping(value = "add/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String addEvent(@PathVariable("id") int id , @RequestParam(name = "time", required = false) String time,
                           @RequestParam(name = "end", required = false) String end,
                           Authentication authentication, Model model){
        user =  userService.makeUserDto(authentication);
        model.addAttribute("user", user);

        User recipient =  userService.getUserById(id);

        if(!model.containsAttribute("event")){
            EventDto eventDto = new EventDto();
            eventDto.setEventStart(time);
            if(recipient.getStaffId() != null){
                eventDto.setLocation(recipient.getStaffId().getRoom());
            }

            System.out.println(eventDto.getEventStart());

            LocalDateTime localDateTime = LocalDateTime.parse(time);

            if(end != null){
                localDateTime = LocalDateTime.parse(end);
            }else{
                localDateTime = localDateTime.plusMinutes(30);
            }

            eventDto.setEventEnd(localDateTime.toString());
            model.addAttribute("event", eventDto);
        }


        model.addAttribute("recipient", recipient);

        System.out.println(user.getUserId());

        return userTemplatePrefix + "addEvent";
    }



    @PostMapping("add")
    public String addEvent(@ModelAttribute("event") @Valid EventDto event, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) throws ParseException {

        if(!eventValidation.timeValidation(event.getEventStart())){
            bindingResult.reject("eventStart","Error with time");
        }
        if(!eventValidation.timeValidation(event.getEventEnd())){
            bindingResult.reject("eventEnd","Error with time");
        }

        //Save the event
        if(bindingResult.hasErrors()){
            redirectAttributes.addAttribute("time", event.getEventStart());
            redirectAttributes.addAttribute("end", event.getEventEnd());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", bindingResult);
            redirectAttributes.addFlashAttribute("event", event);
            return "redirect:/event/add/" + event.getRecipientUserId().getUserId();
        }
        if(event.getRecurringLength() != null){
            eventService.addRecurring(event, user);
        } else{
            eventService.add(event, user);
        }
        //Redirect to the homepage
        return "redirect:/home";
    }

    @GetMapping("view/{id}")
    public String viewEvent(@PathVariable("id") int id, Model model, Authentication authentication){
        user =  userService.makeUserDto(authentication);

        Event event = eventService.getByEventId(id);
        if(event.getCreatorUserId().getUserId() != user.getUserId()
                && event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }

        if(user.getUserId() == event.getRecipientUserId().getUserId()){
            model.addAttribute("receive", true);
        }else{
            model.addAttribute("receive", false);
        }
        model.addAttribute("event", eventService.getByEventId(id));

        return userTemplatePrefix + "viewEvent";
    }


    @GetMapping("edit/{id}")
    public String editEvent(@PathVariable("id") int id, Model model, Authentication authentication){
        user =  userService.makeUserDto(authentication);
        Event event = eventService.getByEventId(id);

        System.out.println(user.getUserId());

        if(event.getCreatorUserId().getUserId() != user.getUserId()
                && event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }

        EventDto eventDto = new EventDto(event);

        eventDto.setEventStart(eventService.changeTimestapToString(event.getEventStart()));

        eventDto.setEventEnd(eventService.changeTimestapToString(event.getEventEnd()));

        System.out.println(eventDto.toString());

        model.addAttribute("event", eventDto);

        return userTemplatePrefix + "editEvent";
    }

    @PostMapping("edit")
    public String editEvent(@ModelAttribute @Valid EventDto event, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return userTemplatePrefix + "editEvent";
        }
        eventService.updateEvent(event);
        return "redirect:/event/view/" + event.getEventId() + "?updated";
    }

    @GetMapping("cancel/{id}")
    public String deleteEvent(@PathVariable("id") int id, Model model, Authentication authentication){
        user =  userService.makeUserDto(authentication);
        Event event = eventService.getByEventId(id);
        if(event.getCreatorUserId().getUserId() != user.getUserId()
                && event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }
        eventService.cancelEvent(id);

        return "redirect:/home?cancel";
    }

    @GetMapping("/accept/{id}")
    public String acceptEvent(@PathVariable("id") int id, Model model, Authentication authentication){

        eventService.acceptEvent(id);

        return "redirect:/event/view/" + id + "?accepted";
    }

}
