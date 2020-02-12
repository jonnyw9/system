package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByCreatorUserId(User user);

    Optional<Event> findEventByEventId(int id);

}
