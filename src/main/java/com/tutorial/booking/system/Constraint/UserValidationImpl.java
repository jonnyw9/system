/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.Service.UserService;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.sql.Time;


@Component
public class UserValidationImpl implements UserValidation {

    @Autowired
    UserService userService;

    @Override
    public BindingResult validate(UserDto userDto, BindingResult bindingResult){
        User existing = userService.getUserByEmail(userDto.getEmail());
        if(existing != null){
            bindingResult.rejectValue("email", "exisiting.email",
                    "There is already an account registered to that email address.");
        }
        if(!userDto.getPassword().equals(userDto.getConfirmPassword())){
            bindingResult.rejectValue("password", "password.mismatch",
                    "The passwords do not match.");
        }

        if(!userDto.isStaff() && !userDto.isStudent()){
            bindingResult.rejectValue("student","" ,"You must select student or staff or both.");
        }

        if(userDto.isStaff()){
            if(userDto.getRoom().isEmpty()){
                bindingResult.rejectValue("room","" ,"You must enter your room.");
            }
            if(userDto.getStartTime().isEmpty()){
                bindingResult.rejectValue("startTime", "" ,"Please enter a start time.");
            }
            if(userDto.getEndTime().isEmpty()){
                bindingResult.rejectValue("endTime", "" ,"Please enter a start time.");
            }
            try{
                Time time = Time.valueOf(userDto.getStartTime() + ":00");
            }catch (Exception e){
                bindingResult.rejectValue("startTime", "" ,"Invalid Start Time.");
            }
            try{
                Time time = Time.valueOf(userDto.getEndTime() + ":00");
            }catch (Exception e){
                bindingResult.rejectValue("endTime", "" ,"Invalid End Time.");
            }
        }
        if(userDto.isStudent()){
            if(userDto.getStudentNumber().isEmpty()){
                bindingResult.rejectValue("studentNumber", "" ,"Please enter a student number.");
            }
        }
        return bindingResult;
    }
}
