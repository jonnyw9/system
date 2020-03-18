/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Notification;
import com.tutorial.booking.system.model.User;

import java.util.List;

public interface NotificationService {
    void saveNotification(Notification notification);

    void eventAddedCreator(Event event);

    void eventAddedRecipient(Event event);

    void eventRecurringAdded(User user);

    void eventCancelled(Event event);

    void eventUpdated(Event event, String locationBefore);

    void eventLocationChanged(Event event);

    void eventAccepted(Event event);

    void accountCreated(User user);

    void passwordChanged(User user);

    List<Notification> getUserNotifications(int id);

    Notification getNotificationById(int id);

    String timeParse(String time);

    void readAllUnreadNotifications(UserDto userDto);

    void notifyUpcomingEvent(Event event, String time);

    void deleteNotification(Notification notification);
}
