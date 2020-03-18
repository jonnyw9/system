/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Calendar;
import com.tutorial.booking.system.model.Event;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Handles requests and logic to do with the events managed by the system. This class acts as the middle man between
 * the controllers and the repository, as well as handling some other logic which is pertinent to the events</p>
 * @author Jonathan Watt
 * @version 1.0.0
 */
@Service
public interface EventService {

    /**
     * <p>Adds an events to the database via the repository.</p>
     * @param eventDto The Data transfer object for the event wished to be added.
     * @param userDto The user who is adding the events.
     * @return The Event that has just been created so that notifications can be sent, and testing can be done.
     */
    Event add(EventDto eventDto, UserDto userDto);

    /**
     * <p>Method which adds events for the length of time (weeks) which the user has requested the recurrence of the event
     * last.</p>
     * @param eventDto The overall details for the events to be added.
     * @param userDto The user who is booking the recurring events.
     * @return a list of events that have been added which are recurring. For testing purposes.
     */
    List<Event> addRecurring(EventDto eventDto, UserDto userDto);

    /**
     * <p>Gets a list of events that are attached to a given user</p>
     * @param userId The id of the user in question.
     * @return The List of events attached to that user.
     */
    List<Event> getEventsForUser(int userId);
    Event getByEventId(int id);
    String changeTimestampToString(Timestamp time);
    void updateEvent(EventDto eventDto);
    void cancelEvent(int eventId);
    ArrayList<Timestamp> convertStringToTimeStamp(EventDto eventDto);
    ArrayList<Timestamp> calulateFreeTimeForTheWeek(Calendar calendar);
    void acceptEvent(int id);
    List<Event> listEventsNearTimeEventUsers(EventDto eventDto);
    @Scheduled(fixedRate = 120000)
    void CheckUpcomingEvents();
    void notifyUsersOfUpcomingEvents(List<Event> events, String time);
}
