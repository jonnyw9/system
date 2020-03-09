package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByCreatorUserId(User user);

    List<Event> findByRecipientUserId(User user);

    //List<Event> findByCreatorUserIdAndRecipientUserId(User user);


    Optional<Event> findEventByEventId(int id);

    List<Event> findEventByEventStartBeforeAndEventStartAfter(Timestamp before, Timestamp after);

    @Modifying
    @Query("UPDATE Event e SET e.title = :title, e.description = :description, e.eventStart = :eventStart," +
          " e.eventEnd = :eventEnd WHERE e.eventId = :eventId")
    void updateEvent(@Param("eventId") int eventId, @Param("title") String title,
                     @Param("description") String description, @Param("eventStart")Timestamp eventStart,
                     @Param("eventEnd")Timestamp eventEnd);
}
