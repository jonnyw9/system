/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;

import java.util.List;

/**
 * <p>Handles the creation, deletion and everything involved with notifications.</p>
 * @author Jonathan Watt
 */
public interface NotificationService {
    /**
     * <p>Saves a notification to the database via the repository.</p>
     * @param notification - The notification to be saved.
     */
    void saveNotification(Notification notification);

    /**
     * <p>Creates a notification for the creator of the event.</p>
     * @param event - The event created.
     */
    void eventAddedCreator(Event event);

    /**
     * <p>Creates a notification for the recipient of the event.</p>
     * @param event - The event recipient.
     */
    void eventAddedRecipient(Event event);

    /**
     * <p>Creates a notification for the creation of a recurring event.</p>
     * @param user - The user who booked the recurring event.
     */
    void eventRecurringAdded(User user);

    /**
     * <p>Creates a notification for the cancellation of an event.</p>
     * @param event - The event to be cancelled.
     * @return A list of the notifications sent. For testing purposes.
     */
    List<Notification> eventCancelled(Event event);

    /**
     * <p>Creates a notification when details of the notification have been updated.</p>
     * @param event - The event which has been updated.
     * @param locationBefore - The location of the event before it was changed.
     */
    void eventUpdated(Event event, String locationBefore);

    /**
     * <p>Creates a notification if the location has been changed on an event.</p>
     * @param event - The event which has been changed.
     */
    void eventLocationChanged(Event event);

    /**
     * <p>Creates a notification when someone accepts an event.</p>
     * @param event - Th event which has beeen accepted.
     */
    void eventAccepted(Event event);

    /**
     * <p>Sends a notification when an account has been created.</p>
     * @param user - The user who just created their account.
     */
    void accountCreated(User user);

    /**
     * <p>Sends a notification when someone changes their password.</p>
     * @param user - The user who changed their password.
     */
    void passwordChanged(User user);

    /**
     * <p>Gets the notifications for a given user.</p>
     * @param id - The id of the user to get notifications for.
     * @return - A list of notifications for the User.
     */
    List<Notification> getUserNotifications(int id);

    /**
     * <p>Gets a notification based on the Id given.</p>
     * @param id - The id of the notification to retrieve.
     * @return the notification retrieved.
     */
    Notification getNotificationById(int id);

    /**
     * <p>Replaces the 'T' in a timestamp with a space for readability purposes.</p>
     * @param time - The timestamp to parse.
     * @return - The parsed timestamp.
     */
    String timeParse(String time);

    /**
     * <p>Reads all the unread notifications for the user.</p>
     * @param userDto - The user to read the notifications of.
     */
    void readAllUnreadNotifications(UserDto userDto);

    /**
     * <p>Notifies users if their event is in half an hour or an hour.</p>
     * @param event - The event which is upcoming.
     * @param time - The time until event.
     */
    void notifyUpcomingEvent(Event event, String time);

    /**
     * <p>Deletes a notification.</p>
     * @param notification - The notification to be deleted.
     */
    void deleteNotification(Notification notification);

    /**
     * <p>Returns the number of unread notifications for a user.</p>
     * @param userDto - The user to return notifications for.
     * @return - The number of unread notifications that user has.
     */
    int noUnreadNotifications(UserDto userDto);
}
