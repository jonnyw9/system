/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.dto.PasswordDto;
import com.tutorial.booking.system.dto.UserDto;
import org.springframework.validation.BindingResult;

/**
 * <p>The class which handles the validation of the events</p>
 * @author Jonathan Watt
 */
public interface PasswordValidation {
    /**
     *
     * @param passwordDto - The password to validate.
     * @param bindingResult - The bindingResult to add errors too.
     * @param user - The user to whom the password belongs
     * @return The binding result with potentially some errors.
     */
    BindingResult validate(PasswordDto passwordDto, BindingResult bindingResult, UserDto user);
}
