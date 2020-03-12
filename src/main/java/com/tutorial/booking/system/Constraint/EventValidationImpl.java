package com.tutorial.booking.system.Constraint;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class EventValidationImpl implements EventValidation{

    @Override
    public boolean timeValidation(String time) {
        if(time == null){
            return false;
        }
        if(time.isEmpty()){
            return false;
        }
        int minutes = Integer.parseInt(time.substring(14, 15));

        if(minutes != 0){
            if(minutes != 30){
                return false;
            }
        }

        minutes = Integer.parseInt(time.substring(11,12));

        if(minutes < 0){
            if(minutes > 23){
                return false;
            }

        }
        return true;
    }

    @Override
    public boolean timeConflictCheck(String eventStart, String eventEnd) {
        return false;
    }
}
