/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.dto.PasswordDto;
import com.tutorial.booking.system.dto.UserDto;
import org.springframework.validation.BindingResult;

public interface PasswordValidation {
    BindingResult validate(PasswordDto passwordDto, BindingResult bindingResult, UserDto user);
}
