/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import com.tutorial.booking.system.Service.UserServiceImpl;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.User;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.sql.Time;


@Component
public class UserValidationImpl implements UserValidation {

    @Autowired
    UserServiceImpl userService;

    @Override
    public BindingResult validate(UserDto userDto, BindingResult bindingResult){

        //Validate the name and email.
        bindingResult = validateName(userDto.getFirstName(), userDto.getLastName(), bindingResult);
        bindingResult = validateEmail(userDto.getEmail(), bindingResult);

        //Check if the user is staff and/or student.
        if(!userDto.isStaff() && !userDto.isStudent()){
            //If neither make error.
            bindingResult.rejectValue("student","" ,"You must select student or staff or both.");
        }

        //IF the user is a staff member
        if(userDto.isStaff()){
            //Validate the room and times
            bindingResult = validateRoom(userDto.getRoom(), bindingResult);
            bindingResult = validateTimes(userDto.getStartTime(), userDto.getEndTime(), bindingResult);
        }
        //If the user is a student
        if(userDto.isStudent()){
            //Validate the student number.
            if(userDto.getStudentNumber().isEmpty()){
                bindingResult.rejectValue("studentNumber", "" ,"Please enter a student number.");
            }
        }
        return bindingResult;
    }

    @Override
    public BindingResult validateEmail(String email, BindingResult bindingResult){
        User existing = userService.getUserByEmail(email);
        //Check if the email already exists
        if(existing != null){
            bindingResult.rejectValue("email", "exisiting.email",
                    "There is already an account registered to that email address.");
        }
        return bindingResult;
    }

    @Override
    public BindingResult validateName(String firstname, String lastname, BindingResult bindingResult){
        //Check if the names are null
        if(firstname == null){
            bindingResult.rejectValue("firstName", "null.name",
                    "Please fill in your first name.");
        }

        if(lastname == null){
            bindingResult.rejectValue("lastName", "null.name",
                    "Please fill in your last name.");
        }
        //Check they are at least 2 characters long
        if(firstname.length() < 2){
            bindingResult.rejectValue("firstName", "name.too.small",
                    "Your first name is too small!");
        }
        if(lastname.length() < 2){
            bindingResult.rejectValue("lastName", "name.too.small",
                    "Your last name is too small!");
        }

        return bindingResult;
    }

    @Override
    public BindingResult validateTimes(String startTime, String endTime, BindingResult bindingResult){
        //Check the times are not empty
        if(startTime.isEmpty()){
            bindingResult.rejectValue("startTime", "" ,"Please enter a start time.");
        }
        if(endTime.isEmpty()){
            bindingResult.rejectValue("endTime", "" ,"Please enter a start time.");
        }
        //Try parsing the times
        try{
            Time time = Time.valueOf(startTime + ":00");
        }catch (Exception e){
            //If it fails the start time is invalid
            bindingResult.rejectValue("startTime", "" ,"Invalid Start Time.");
        }
        try{
            Time time = Time.valueOf(endTime + ":00");
        }catch (Exception e){
            bindingResult.rejectValue("endTime", "" ,"Invalid End Time.");
        }
        return bindingResult;
    }
    @Override
    public BindingResult validateRoom(String room, BindingResult bindingResult){
        //Check the room is not empty
        if(room.isEmpty()){
            bindingResult.rejectValue("room","" ,"You must enter your room.");
        }
        return bindingResult;
    }
}
