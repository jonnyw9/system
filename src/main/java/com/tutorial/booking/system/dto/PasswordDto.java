package com.tutorial.booking.system.dto;

public class PasswordDto {

    private String currentPassword;

    private String password;
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
