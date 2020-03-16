/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.dto.UserDto;
import org.springframework.validation.BindingResult;

public interface UserValidation {
    BindingResult validate(UserDto userDto, BindingResult bindingResult);
    BindingResult validateEmail(String email, BindingResult bindingResult);
    BindingResult validateName(String firstname, String lastname, BindingResult bindingResult);
    BindingResult validateTimes(String startTime, String endTime, BindingResult bindingResult);
    BindingResult validateRoom(String room, BindingResult bindingResult);
}
