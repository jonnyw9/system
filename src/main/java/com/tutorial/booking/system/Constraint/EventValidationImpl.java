/*
 * Copyright (c) 2020. To JWIndustries
 */

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
        //Checks the time is not null
        if(time == null){
            return false;
        }
        //Checks the time is not empty
        if(time.isEmpty()){
            return false;
        }
        int minutes = Integer.parseInt(time.substring(14, 16));

        //Checks the minutes are either 00 or 30. So on the hour or half past
        if(minutes != 0){
            return minutes == 30;
        }
        return true;
    }

    @Override
    public boolean timeConflictCheck(EventDto eventDto){
        return eventService.listEventsNearTimeEventUsers(eventDto);
    }

    @Override
    public BindingResult validateTimeConflicts(EventDto eventDto, BindingResult bindingResult){
        if(timeConflictCheck(eventDto)){
            //If the time has a conflict, bind an error
            bindingResult.reject("time.conflict","You have a conflict with the times suggested.");
        }
        return bindingResult;
    }

    @Override
    public BindingResult validate(EventDto eventDto, BindingResult bindingResult){
        //If the time is not valid, bind an error
        if(!timeValidation(eventDto.getEventStart())){
            bindingResult.rejectValue("eventStart","time.error","Time not allowed!");
        }
        if(!timeValidation(eventDto.getEventEnd())){
            bindingResult.rejectValue("eventEnd","time.error", "Time not allowed!");
        }


        return bindingResult;
    }
}
