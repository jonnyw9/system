/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.model;

import javax.persistence.*;


/**
 * <p>Entity class for the Student table. The Student table will map to this class.</p>
 * @author Jonathan Watt
 */
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;


    private String studentNumber;

    public Student() {
    }

    public Student(int studentId, String studentNumber) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
