/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.dto.UserDto;
import org.springframework.validation.BindingResult;

public interface UserValidation {
    BindingResult validate(UserDto userDto, BindingResult bindingResult);
}
