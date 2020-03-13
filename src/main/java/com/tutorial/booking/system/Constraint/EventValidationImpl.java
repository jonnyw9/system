package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.Service.EventService;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Component
public class EventValidationImpl implements EventValidation{

    @Autowired
    EventService eventService;

    @Override
    public boolean timeValidation(String time) {
        if(time == null){
            return false;
        }
        if(time.isEmpty()){
            return false;
        }
        int minutes = Integer.parseInt(time.substring(14, 16));

        System.out.println(minutes);

        if(minutes != 0){
            if(minutes != 30){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean timeConflictCheck(EventDto eventDto){
        List<Event> events = eventService.listEventsNearTimeEventUsers(eventDto);
        for (int i = 0; i < events.size(); i++) {
            System.out.println("Events: " + events.get(i));
        }

        if(!eventService.listEventsNearTimeEventUsers(eventDto).isEmpty()){
            return false;
        }
        return true;
    }
}
