package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
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

    @Query("select e from Event e where e.eventStart = ?1 and e.creatorUserId = ?2 or e.eventStart = ?1 and e.recipientUserId = ?3 ")
    List<Event> findFirstByEventStartEqualsAndCreatorUserIdOrRecipientUserId(Timestamp eventStart, User creatorUserId, User RecipientUserId);


    @Query("select e from Event e where e.eventStart > ?1 AND e.eventEnd < ?2 AND e.creatorUserId = ?3 OR " +
            "e.eventStart > ?1 AND e.eventEnd < ?2 AND e.recipientUserId = ?4")
    List<Event> findFirstByEventStartBeforeAndEventEndAfterAndCreatorUserIdOrRecipientUserId(Timestamp eventStart, Timestamp eventEnd, User creatorUserId, User recipientUserId);

    @Query("select e from Event e where e.creatorUserId = ?1 or e.recipientUserId = ?2")
    List<Event> findAllByCreatorUserIdAndRecipientUserId(User creatorUserId, User recipientUserId);

    @Query("select e from Event e where e.eventStart > ?1 and e.eventStart < ?2")
    List<Event> findAllByEventStartInXMins(Timestamp timestamp1, Timestamp timestamp2);
}

