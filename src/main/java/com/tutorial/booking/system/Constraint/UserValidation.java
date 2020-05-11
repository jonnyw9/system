/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.dto.UserDto;
import org.springframework.validation.BindingResult;

/**
 * <p>The class which handles the validation of the users. With this class. A lot of components are split into different
 * methods, unlike other validation class. This is because you can edit separate parts of the user account.</p>
 * @author Jonathan Watt
 */
public interface UserValidation {
    /**
     * <p>Handles the validation of the user.</p>
     * @param userDto - The user to validate.
     * @param bindingResult - The bindingResult to add errors too.
     * @return The binding result with potentially some errors.
     */
    BindingResult validate(UserDto userDto, BindingResult bindingResult);

    /**
     * <p>Validates the email for the users.</p>
     * @param email - The email to validate.
     * @param bindingResult - The bindingResult to add errors too.
     * @return The binding result with potentially some errors.
     */
    BindingResult validateEmail(String email, BindingResult bindingResult);

    /**
     * <p>Validates the users name</p>
     * @param firstname - The firstname to validate
     * @param lastname - The lastname to validate
     * @param bindingResult - The bindingResult to add errors too.
     * @return The binding result with potentially some errors.
     */
    BindingResult validateName(String firstname, String lastname, BindingResult bindingResult);

    /**
     * <p>Validates the start and end times of the staff member</p>
     * @param startTime - The start time.
     * @param endTime - The end time.
     * @param bindingResult - The bindingResult to add errors too.
     * @return The binding result with potentially some errors.
     */
    BindingResult validateTimes(String startTime, String endTime, BindingResult bindingResult);

    /**
     * <p>Validates the room of the staff member.</p>
     * @param room - The room to validate
     * @param bindingResult - The bindingResult to add errors too.
     * @return The binding result with potentially some errors.
     */
    BindingResult validateRoom(String room, BindingResult bindingResult);
}
