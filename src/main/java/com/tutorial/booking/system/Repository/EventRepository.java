package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
