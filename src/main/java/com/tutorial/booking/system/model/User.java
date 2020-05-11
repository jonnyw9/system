/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * <p>Entity class for the User table. The User table will map to this class.</p>
 * @author Jonathan Watt
 */
@Entity
@Table(name = "bs_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String firstName;
    private String email;
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passwordId", referencedColumnName = "passwordId")
    private Password password;

    private boolean active;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    private Roles roleId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staffId", referencedColumnName = "staffId")
    private Staff staffId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "studentId", referencedColumnName = "studentId")
    private Student studentId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "calendarId", referencedColumnName = "calendarId")
    private Calendar calendarId;

    public User() {
    }

    public User(int userId, String email, String firstName, String lastName, Password password, boolean active, Roles roleId,
                Calendar calendarId, Staff staffId, Student studentId) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.active = active;
        this.roleId = roleId;
        this.calendarId = calendarId;
        this.staffId = staffId;
        this.studentId = studentId;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Password getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Roles getRoleId() {
        return roleId;
    }

    public void setRoleId(Roles roleId) {
        this.roleId = roleId;
    }

    public Staff getStaffId() {
        return staffId;
    }

    public void setStaffId(Staff staffId) {
        this.staffId = staffId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public Calendar getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Calendar calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return
                isActive() == user.isActive() &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getRoleId(), user.getRoleId()) &&
                Objects.equals(getStaffId(), user.getStaffId()) &&
                Objects.equals(getStudentId(), user.getStudentId()) &&
                Objects.equals(getCalendarId(), user.getCalendarId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFirstName(), getEmail(), getLastName(), getPassword(), isActive(), getRoleId(), getStaffId(), getStudentId(), getCalendarId());
    }
}
