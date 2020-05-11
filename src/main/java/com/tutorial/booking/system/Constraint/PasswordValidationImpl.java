/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.Repository.PasswordRepository;
import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.Service.UserServiceImpl;
import com.tutorial.booking.system.dto.PasswordDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class PasswordValidationImpl implements PasswordValidation {

    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    UserService userService;

    @Override
    public BindingResult validate(PasswordDto passwordDto, BindingResult bindingResult, UserDto user) {
        //Gets the password
        Password password = passwordRepository.getOne(
                userService.getUserById(user.getUserId()).getPassword().getPasswordId());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //Checks if the current password is correct, binds error if not.
        if(!passwordEncoder.matches(passwordDto.getCurrentPassword(), password.getPassword())){
            bindingResult.rejectValue("currentPassword", "no.match", "Current Password Incorrect.");
        }
        //Checks if the passwords match.
        if(!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())){
            bindingResult.rejectValue("password", "no.match", "Passwords do not match.");
        }

        return bindingResult;
    }
}
