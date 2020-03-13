package com.tutorial.booking.system.Controller;


import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventApiEntity;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event/")
public class EventApiController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;


    @GetMapping("getall/{id}")
    @ResponseBody
    public ResponseEntity<List<EventApiEntity>> findAll(@PathVariable int id){

        String urlBase = "https://jwbookingsystem.herokuapp.com/event/view/";
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

        String urlBase = "https://jwbookingsystem.herokuapp.com/event/add/";

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
