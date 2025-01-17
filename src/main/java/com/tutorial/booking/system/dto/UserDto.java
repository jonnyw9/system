/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.dto;

import com.tutorial.booking.system.model.User;
import org.springframework.security.core.parameters.P;

import javax.validation.constraints.*;
import java.util.Objects;

/**
 * <p>A class used during the runtime which holds elements of the User table but which is not mapped to it.</p>
 * @author Jonathan Watt
 */
public class UserDto {

    private int userId;

    @NotNull
    @NotEmpty(message = "Please give you first name.")
    @Size(min = 1, message = "Your first name is too small!")
    private String firstName;

    @NotNull
    @NotEmpty(message = "Please give you last name.")
    @Size(min = 1, message = "Your last name is too small!")
    private String lastName;

    @NotNull
    @NotEmpty
    @Email(message = "Please provide a valid email address")
    private String email;


    @NotEmpty(message = "Please type in a password.")
    @Size(min = 8, message = "You password must be 8 characters or more.")
    private String password;
    @NotEmpty(message = "Please type in a confirm password.")
    private String confirmPassword;


    private boolean student;
    private boolean staff;

    private String room;
    private String studentNumber;

    private String startTime;
    private String endTime;

    public UserDto() {
    }

    public UserDto(User user){
        this.student = user.getRoleId().isStudent();
        this.staff = user.getRoleId().isStaff();
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        if(user.getStaffId() != null){
            this.startTime = user.getCalendarId().getDayStartTime().toString().substring(0, 5);
            this.endTime = user.getCalendarId().getDayEndTime().toString().substring(0, 5);
            this.room = user.getStaffId().getRoom();
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", student=" + student +
                ", staff=" + staff +
                ", room='" + room + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return getUserId() == userDto.getUserId() &&
                isStudent() == userDto.isStudent() &&
                isStaff() == userDto.isStaff() &&
                Objects.equals(getFirstName(), userDto.getFirstName()) &&
                Objects.equals(getLastName(), userDto.getLastName()) &&
                Objects.equals(getEmail(), userDto.getEmail()) &&
                Objects.equals(getPassword(), userDto.getPassword()) &&
                Objects.equals(getConfirmPassword(), userDto.getConfirmPassword()) &&
                Objects.equals(getRoom(), userDto.getRoom()) &&
                Objects.equals(getStudentNumber(), userDto.getStudentNumber()) &&
                Objects.equals(getStartTime(), userDto.getStartTime()) &&
                Objects.equals(getEndTime(), userDto.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFirstName(), getLastName(), getEmail(), getPassword(), getConfirmPassword(), isStudent(), isStaff(), getRoom(), getStudentNumber(), getStartTime(), getEndTime());
    }
}
