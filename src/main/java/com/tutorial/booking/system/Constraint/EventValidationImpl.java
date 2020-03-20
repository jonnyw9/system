package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.Service.EventServiceImpl;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
public class EventValidationImpl implements EventValidation{

    @Autowired
    EventServiceImpl eventService;

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

    public BindingResult validateTimeConflicts(EventDto eventDto, BindingResult bindingResult){
        if(!timeConflictCheck(eventDto)){
            bindingResult.reject("time.conflict", "You have a conflict with the times suggested.");
        }
        return bindingResult;
    }

    @Override
    public BindingResult validate(EventDto eventDto, BindingResult bindingResult){

        if(!timeValidation(eventDto.getEventStart())){
            bindingResult.rejectValue("eventStart","time.error","Time not allowed!");
        }
        if(!timeValidation(eventDto.getEventEnd())){
            bindingResult.rejectValue("eventEnd","time.error", "Time not allowed!");
        }


        return bindingResult;
    }
}
