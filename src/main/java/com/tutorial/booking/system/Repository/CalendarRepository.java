package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
}
