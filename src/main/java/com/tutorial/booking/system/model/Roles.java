/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.model;

import javax.persistence.*;

/**
 * <p>Entity class for the Roles table. The Roles table will map to this class.</p>
 * @author Jonathan Watt
 */
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;
    private boolean staff;
    private boolean student;
    private boolean admin;

    public Roles() {
    }

    public Roles(int roleId, boolean staff, boolean student, boolean admin) {
        this.roleId = roleId;
        this.staff = staff;
        this.student = student;
        this.admin = admin;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
