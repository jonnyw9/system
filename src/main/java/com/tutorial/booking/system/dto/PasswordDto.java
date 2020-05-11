/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>A class used during the runtime which holds elements of the Password table but which is not mapped to it.</p>
 * @author Jonathan Watt
 */
public class PasswordDto {

    @NotNull
    @NotEmpty(message = "Please type in a password.")
    private String currentPassword;

    @NotNull
    @NotEmpty(message = "Please type in a password.")
    @Size(min = 8, message = "You password must be 8 characters or more.")
    private String password;
    
    @NotNull
    @NotEmpty(message = "Please type in a confirm password.")
    private String confirmPassword;

    public PasswordDto() {
    }

    public PasswordDto(String currentPassword, String password, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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

    @Override
    public String toString() {
        return "PasswordDto{" +
                "currentPassword='" + currentPassword + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
