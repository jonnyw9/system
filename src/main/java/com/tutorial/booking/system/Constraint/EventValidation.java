package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.dto.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public interface EventValidation{
    public boolean timeValidation(String time);

    public boolean timeConflictCheck(EventDto eventDto);

    public BindingResult validate(EventDto eventDto, BindingResult bindingResult);
}
