/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.dto.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * <p>The class which handles the validation of the events.</p>
 * @author Jonathan Watt
 */
@Component
public interface EventValidation{
    /**
     * <p>Validates whether the time is valid for an event.</p>
     * @param time - The time to validate
     * @return - a boolean value with true being it is valid and false being it isn't
     */
    boolean timeValidation(String time);

    /**
     * <p>Calls the method in the service which checks if there are any time conflicts.</p>
     * @param eventDto - The event to check.
     * @return - a boolean true for a conflict, false for no conflict.
     */
    boolean timeConflictCheck(EventDto eventDto);

    /**
     * <p>Calls the validation of the time conflict.</p>
     * @param eventDto - The event to validate.
     * @param bindingResult - The bindingResult to add errors too.
     * @return The binding result with potentially some errors.
     */
    BindingResult validateTimeConflicts(EventDto eventDto, BindingResult bindingResult);

    /**
     * <p>Calls the validation of the time.</p>
     * @param eventDto - The event to validate.
     * @param bindingResult - The bindingResult to add errors too.
     * @return The binding result with potentially some errors.
     */
    BindingResult validate(EventDto eventDto, BindingResult bindingResult);
}
