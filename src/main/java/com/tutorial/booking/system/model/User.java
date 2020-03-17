package com.tutorial.booking.system.model;

import javax.persistence.*;
import java.util.List;

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
}
