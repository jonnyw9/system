/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Controller;


import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.EventServiceImpl;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.Service.UserServiceImpl;
import com.tutorial.booking.system.dto.EventApiEntity;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A REST Controller used by the FullCalendar to get events onto the calendar.</p>
 * @author Jonathan Watt
 */
@RestController
@RequestMapping("/api/event/")
public class EventApiController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;


    /**
     * <p>Gets all the Events for a given user Id.</p>
     * @param id - The user id given.
     * @return - The JSON of the eventApiEntity of the list of events for that user.
     */
    @GetMapping("getall/{id}")
    @ResponseBody
    public ResponseEntity<List<EventApiEntity>> findAll(@PathVariable int id){

        //Sets up a URL to view the event
        String urlBase = "/event/view/";
        //Gets the event
        List<Event> events = eventService.getEventsForUser(id);

        //If there are no events respond with not found.
        if(events == null){
            return ResponseEntity.notFound().build();
        }else{
            //Create a list of eventApiEntities to return
            List<EventApiEntity> eventApiReturn = new ArrayList<>();

            //For all the events map the pertinent information to the eventApiEntity
            for(Event event: events){
                EventApiEntity eventApiEntity = new EventApiEntity();
                eventApiEntity.setId(event.getEventId());
                eventApiEntity.setTitle(event.getTitle());
                eventApiEntity.setStart(event.getEventStart().toLocalDateTime().toString());
                eventApiEntity.setEnd(event.getEventEnd().toLocalDateTime().toString());
                eventApiEntity.setUrl(urlBase + String.valueOf(event.getEventId()));
                eventApiReturn.add(eventApiEntity);
            }

            //Return the JSON
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id)
                    .toUri();

            return ResponseEntity.created(uri).body(eventApiReturn);
        }
    }

    @GetMapping("/times/{id}")
    public ResponseEntity<List<EventApiEntity>> getStaffFreeTime(@PathVariable int id){

        User user = userService.getUserById(id);

        List<Timestamp> availableTimes = eventService.calulateFreeTimeForTheWeek(user.getCalendarId());

        List<LocalDateTime> availableDates = new ArrayList<>();

        for(Timestamp timestamp: availableTimes){
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            availableDates.add(localDateTime);
        }

        String urlBase = "/event/add/";

        if(availableDates.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            List<EventApiEntity> eventApiReturn = new ArrayList<>();

            for(LocalDateTime event: availableDates){
                EventApiEntity eventApiEntity = new EventApiEntity();
                eventApiEntity.setTitle("Book");
                eventApiEntity.setStart(event.toString());
                LocalDateTime end = event.plusMinutes(30);
                eventApiEntity.setEnd(end.toString());
                eventApiEntity.setUrl(urlBase + String.valueOf(id) + "?time=" + event.toString());
                eventApiReturn.add(eventApiEntity);
            }

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id)
                    .toUri();

            return ResponseEntity.created(uri).body(eventApiReturn);
        }
    }
}
