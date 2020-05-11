/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <p>A repository for the password table on the database. All database requests for that table go through this class.</p>
 */
public interface PasswordRepository extends JpaRepository<Password, Integer> {
}
