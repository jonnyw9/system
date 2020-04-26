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

    /**
     * <p>Gets the event by the Id passed in </p>
     * @param id - The ID of the event
     * @return The event if it exists or null if it doesn't
     */
    Event getByEventId(int id);

    /**
     * <p>Returns a string parsed from a timestamp for use in browsers</p>
     * @param time - The timestamp to be converted to a string
     * @return String - the timestamp parsed from the timestamp to be used in browsers.
     */
    String changeTimestampToString(Timestamp time);

    /**
     *<p>Updates a given event with the new details.</p>
     * @param eventDto - The eventDTO with the new user details on
     * @return Updated event
     */
    Event updateEvent(EventDto eventDto);

    /**
     * <p>Deletes a given event</p>
     * @param eventId - the Id of the event to be deleted
     */
    void cancelEvent(int eventId);

    /**
     * <p>Converts the start and end times of an eventDTO to timestamps</p>
     * @param eventDto - The eventDTO with the strings to be converted in
     * @return An ArrayList containing the converted strings to timestamps
     */
    ArrayList<Timestamp> convertStringToTimeStamp(EventDto eventDto);
    ArrayList<Timestamp> calulateFreeTimeForTheWeek(Calendar calendar);

    /**
     * <p>Accept a given event</p>
     * @param id - The id of the event to be accepted
     */
    void acceptEvent(int id);

    /**
     * <p>Returns a list of events which overlap with the event that is attempting to be booked</p>
     * @param eventDto - The event which is intended to be booked.
     * @return An ArrayList containing any events which overlap with the one to be booked.
     */
    boolean listEventsNearTimeEventUsers(EventDto eventDto);

    /**
     * <p>A method which runs every 2 minutes which checks if an event is 30 minutes away and
     * if an event is one hour away.</p>
     */
    @Scheduled(fixedRate = 120000)
    void CheckUpcomingEvents();

    /**
     * <p>Notifies a user if their event is 30 minutes or 1 hour away.</p>
     * @param events - List of events which are 30 minutes or one hour away.
     * @param time - how long the event is away. 1 hour or 30 minutes
     */
    void notifyUsersOfUpcomingEvents(List<Event> events, String time);
}
