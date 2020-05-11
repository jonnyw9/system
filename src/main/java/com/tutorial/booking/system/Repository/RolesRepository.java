/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>A repository for the roles table on the database. All database requests for that table go through this class.</p>
 */
public interface RolesRepository extends JpaRepository<Roles, Integer> {
}
