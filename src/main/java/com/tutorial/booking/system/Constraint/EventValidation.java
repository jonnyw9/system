package com.tutorial.booking.system.Constraint;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public interface EventValidation{
    public boolean timeValidation(String time);

    public boolean timeConflictCheck(String eventStart, String eventEnd);
}
