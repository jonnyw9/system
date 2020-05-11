/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>A repository for the calendar table on the database. All database requests for that table go through this class.</p>
 */
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
}
