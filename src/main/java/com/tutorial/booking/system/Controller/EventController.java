package com.tutorial.booking.system.Controller;


import com.tutorial.booking.system.Constraint.EventValidation;
import com.tutorial.booking.system.Service.*;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;

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
    NotificationService notificationService;


    @Autowired
    EventValidation eventValidation;

    @RequestMapping(value = "add/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String addEvent(@PathVariable("id") int id , @RequestParam(name = "time", required = false) String time,
                           @RequestParam(name = "end", required = false) String end,
                           Authentication authentication, Model model){
        user =  userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

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
                           RedirectAttributes redirectAttributes) {

        bindingResult = eventValidation.validate(event, bindingResult);
        bindingResult = eventValidation.validateTimeConflicts(event, bindingResult);

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
            notificationService.eventRecurringAdded(userService.getUserById(user.getUserId()));
        } else{
            Event event1 = eventService.add(event, user);
            notificationService.eventAddedCreator(event1);
            if(event.getCreatorUserId().getUserId() != event.getRecipientUserId().getUserId()){
                notificationService.eventAddedRecipient(event1);
            }
        }
        //Redirect to the homepage
        return "redirect:/home";
    }

    @GetMapping("view/{id}")
    public String viewEvent(@PathVariable("id") int id, Model model, Authentication authentication){
        user =  userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        Event event = eventService.getByEventId(id);
        if(event == null){
            return "redirect:/home?eventNotFound";
        }
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

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        Event event = eventService.getByEventId(id);

        System.out.println(user.getUserId());

        if(event.getCreatorUserId().getUserId() != user.getUserId()
                && event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }

        if(!model.containsAttribute("event")){
            EventDto eventDto = new EventDto(event);

            eventDto.setEventStart(eventService.changeTimestampToString(event.getEventStart()));

            eventDto.setEventEnd(eventService.changeTimestampToString(event.getEventEnd()));

            System.out.println("Start: " + eventDto.getEventStart() + " || End: " + eventDto.getEventEnd());

            model.addAttribute("event", eventDto);
        }

        return userTemplatePrefix + "editEvent";
    }

    @PostMapping("edit")
    public String editEvent(@ModelAttribute @Valid EventDto event, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        System.out.println("Start: " + event.getEventStart() + " || End: " + event.getEventEnd());

        bindingResult = eventValidation.validate(event, bindingResult);

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", bindingResult);
            System.out.println(bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("event", event);
            return "redirect:/event/edit/" + event.getEventId();
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
