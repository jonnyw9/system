package com.tutorial.booking.system.Controller;


import com.tutorial.booking.system.Service.EventServiceImpl;
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

@RestController
@RequestMapping("/api/event/")
public class EventApiController {

    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("getall/{id}")
    @ResponseBody
    public ResponseEntity<List<EventApiEntity>> findAll(@PathVariable int id){

        String urlBase = "http://localhost:8080/event/view/";
        List<Event> events = eventService.getEventsForUser(id);

        if(events == null){
            return ResponseEntity.notFound().build();
        }else{
            List<EventApiEntity> eventApiReturn = new ArrayList<>();

            for(Event event: events){
                EventApiEntity eventApiEntity = new EventApiEntity();
                eventApiEntity.setId(event.getEventId());
                eventApiEntity.setTitle(event.getTitle());
                eventApiEntity.setStart(event.getEventStart().toLocalDateTime().toString());
                eventApiEntity.setEnd(event.getEventEnd().toLocalDateTime().toString());
                eventApiEntity.setUrl(urlBase + String.valueOf(event.getEventId()));
                eventApiReturn.add(eventApiEntity);
            }

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

        String urlBase = "http://localhost:8080/event/add/";

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
