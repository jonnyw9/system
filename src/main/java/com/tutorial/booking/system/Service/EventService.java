/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Calendar;
import com.tutorial.booking.system.model.Event;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public interface EventService {

    void add(EventDto eventDto, UserDto userDto, Boolean recurring) throws ParseException;
    void addRecurring(EventDto eventDto, UserDto userDto) throws ParseException;
    List<Event> getEventsForUser(int userId);
    Event getByEventId(int id);
    String changeTimestapToString(Timestamp time);
    void updateEvent(EventDto eventDto);
    void cancelEvent(int eventId);
    ArrayList<Timestamp> convertStringToTimeStamp(EventDto eventDto);
    ArrayList<Timestamp> calulateFreeTimeForTheWeek(Calendar calendar);
    void acceptEvent(int id);
    List<Event> listEventsNearTimeEventUsers(EventDto eventDto);
    void CheckUpcomingEvents();
    void notifyUsersOfUpcomingEvents(List<Event> events, String time);
}
