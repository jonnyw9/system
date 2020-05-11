/*
 * Copyright (c) 2020. To JWIndustries
 */

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

/**
 * <p>A controller for the event class.</p>
 * @author Jonathan Watt
 */
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

    /**
     * <p>Adds the various information needed for the add event form.</p>
     * @param id - The id of the user who is being booked.
     * @param time - The start time of the event.
     * @param end - The end time of the event.
     * @param authentication - The authenticated user using the system.
     * @param model - The model to put the information on.
     * @return The template to render the information on.
     */
    @RequestMapping(value = "add/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public String addEvent(@PathVariable("id") int id , @RequestParam(name = "time", required = false) String time,
                           @RequestParam(name = "end", required = false) String end,
                           Authentication authentication, Model model){
        //Get the user from the authentication
        user =  userService.makeUserDto(authentication);
        //Add the number of unread notifications for the banner to the model.
        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));
        //Add the user to the model.
        model.addAttribute("user", user);
        //Get the recipient
        User recipient =  userService.getUserById(id);
        //If the user has not been redirected back from the post method.
        if(!model.containsAttribute("event")){
            //Add a new event dto
            EventDto eventDto = new EventDto();
            //Set the start times
            eventDto.setEventStart(time);
            //If the user is booking a staff member set the location to the staff members office
            if(recipient.getStaffId() != null){
                eventDto.setLocation(recipient.getStaffId().getRoom());
            }
            //Parse the time
            LocalDateTime localDateTime = LocalDateTime.parse(time);
            //If there is an end time parse it, if not add 30 minutes to the start time.
            if(end != null){
                localDateTime = LocalDateTime.parse(end);
            }else{
                localDateTime = localDateTime.plusMinutes(30);
            }

            //Add the end time and then add the eventDto the model.
            eventDto.setEventEnd(localDateTime.toString());
            model.addAttribute("event", eventDto);
        }
        //Add the recipient to the model
        model.addAttribute("recipient", recipient);
        //Go to this template
        return userTemplatePrefix + "addEvent";
    }

    /**
     * <p>The post mapping for adding the event.</p>
     * @param event - The eventDTO of the event to be added.
     * @param bindingResult - The binding result for any errors
     * @param redirectAttributes - The redirect attributes incase there is a need for redirect.
     * @return - A redirect to the homepage.
     */
    @PostMapping("add")
    public String addEvent(@ModelAttribute("event") @Valid EventDto event, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        //Validate the event
        bindingResult = eventValidation.validate(event, bindingResult);
        bindingResult = eventValidation.validateTimeConflicts(event, bindingResult);

        //Check for any errors
        if(bindingResult.hasErrors()){
            //If there are errors add the attributes and redirect back to the form.
            redirectAttributes.addAttribute("time", event.getEventStart());
            redirectAttributes.addAttribute("end", event.getEventEnd());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", bindingResult);
            redirectAttributes.addFlashAttribute("event", event);
            return "redirect:/event/add/" + event.getRecipientUserId().getUserId();
        }
        //If the event is recurring
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

    /**
     * <p>Controller to view a particular event.</p>
     * @param id - The id of the event.
     * @param model - The model to add attributes to.
     * @param authentication - The authenticated user.
     * @return The template to show the user.
     */
    @GetMapping("view/{id}")
    public String viewEvent(@PathVariable("id") int id, Model model, Authentication authentication){
        user =  userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);
        //Get the event
        Event event = eventService.getByEventId(id);
        //If the event is not found redirect to homepage with a message
        if(event == null){
            return "redirect:/home?eventNotFound";
        }
        //If the user trying to view the event is not a creator or recipient of the event.
        if(event.getCreatorUserId().getUserId() != user.getUserId()
                && event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }

        //Check if the user viewing the event is the recipient.
        if(user.getUserId() == event.getRecipientUserId().getUserId()){
            model.addAttribute("receive", true);
        }else{
            model.addAttribute("receive", false);
        }
        model.addAttribute("event", eventService.getByEventId(id));

        return userTemplatePrefix + "viewEvent";
    }


    /**
     * <p>A controller to make the form to edit an event.</p>
     * @param id - The id of the event.
     * @param model - The model to add attributes to.
     * @param authentication - The authenticated user.
     * @return - The event edit form.
     */
    @GetMapping("edit/{id}")
    public String editEvent(@PathVariable("id") int id, Model model, Authentication authentication){
        user =  userService.makeUserDto(authentication);

        model.addAttribute("unreadNotifications", notificationService.noUnreadNotifications(user));

        model.addAttribute("user", user);

        Event event = eventService.getByEventId(id);
        //If the user is not part of the event
        if(event.getCreatorUserId().getUserId() != user.getUserId()
                && event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }

        //If the user has not been redirected from the post
        if(!model.containsAttribute("event")){
            EventDto eventDto = new EventDto(event);

            eventDto.setEventStart(eventService.changeTimestampToString(event.getEventStart()));

            eventDto.setEventEnd(eventService.changeTimestampToString(event.getEventEnd()));

            model.addAttribute("event", eventDto);
        }

        return userTemplatePrefix + "editEvent";
    }

    /**
     * <p>Handles the post validation and updating of an event.</p>
     * @param event - The eventDTO of the updated event.
     * @param bindingResult - The bindingResult to add errors too.
     * @param redirectAttributes - The redirectAttributes if needed
     * @return - either the view page of the page, or the edit form again depending on the outcome.
     */
    @PostMapping("edit")
    public String editEvent(@ModelAttribute @Valid EventDto event, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        bindingResult = eventValidation.validate(event, bindingResult);

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.event", bindingResult);
            System.out.println(bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("event", event);
            return "redirect:/event/edit/" + event.getEventId();
        }
        String locationBefore = eventService.getByEventId(event.getEventId()).getLocation();
        Event updatedEvent = eventService.updateEvent(event);

        notificationService.eventUpdated(updatedEvent, locationBefore);
        return "redirect:/event/view/" + event.getEventId() + "?updated";
    }

    /**
     * <p>Cancels an event.</p>
     * @param id - The id of the event
     * @param model - The model to add attributes to.
     * @param authentication - The authenticated user.
     * @return - The homepage with messages depending on the outcome.
     */
    @GetMapping("cancel/{id}")
    public String deleteEvent(@PathVariable("id") int id, Model model, Authentication authentication){
        //Gets the user and event.
        user =  userService.makeUserDto(authentication);
        Event event = eventService.getByEventId(id);
        //If the user isn't part of the event
        if(event.getCreatorUserId().getUserId() != user.getUserId()
                && event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }
        //Cancel the event.
        eventService.cancelEvent(id);

        return "redirect:/home?cancel";
    }

    /**
     * <p>Controller for accepting an event.</p>
     * @param id - The id of the event
     * @param model - the model of the event
     * @param authentication - the authenticated user.
     * @return redirect to the homepage or to the view of the event depending on the outcome.
     */
    @GetMapping("/accept/{id}")
    public String acceptEvent(@PathVariable("id") int id, Model model, Authentication authentication){

        user =  userService.makeUserDto(authentication);
        Event event = eventService.getByEventId(id);

        if(event.getRecipientUserId().getUserId() != user.getUserId()){
            return "redirect:/home?notYourEvent";
        }

        eventService.acceptEvent(id);

        return "redirect:/event/view/" + id + "?accepted";
    }

}
