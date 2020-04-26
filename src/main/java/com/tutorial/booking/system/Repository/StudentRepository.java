/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>A repository for the student table on the database. All database requests for that table go through this class.</p>
 */
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
