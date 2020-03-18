/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.EventRepository;
import com.tutorial.booking.system.dto.EventDto;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Event;
import com.tutorial.booking.system.model.Roles;
import com.tutorial.booking.system.model.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
@SpringBootTest
class EventServiceTest {

    private static Event e1;
    private static Event e2;
    private static EventDto edto1;
    private static EventDto edto2;
    private static User u1;
    private static User u2;
    private static UserDto udto1;
    private static UserDto udto2;
    private static Timestamp t1;
    private static Timestamp t2;
    private static List<Event> events;

    @InjectMocks
    @Spy
    EventService eventService = new EventServiceImpl();

    @Mock
    EventRepository eventRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init(){
        t1 = Timestamp.valueOf(("2020-03-19T09:00"+":00").replace("T", " "));
        t2 = Timestamp.valueOf(("2020-03-19T11:00"+":00").replace("T", " "));
        u1 = new User(10000, "test@test.co.uk", "test", "testl", null,
                true, new Roles(1, true, true, false), null, null, null);
        u2 = new User(100001, "test@test.com", "test1", "testl1", null,
                true, new Roles(1, true, true, false), null, null, null);
        e1 = new Event(1, t1, t2, "test", "test2", u1, u2, "location");
        edto1 = new EventDto(e1);
        edto1.setEventStart("2020-03-19T09:00");
        edto1.setEventEnd("2020-03-19T11:00");
        edto1.setRecurringLength(2);
        udto1 = new UserDto(u1);
    }

    @Test
    void add(){
        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(e1);
        assertEquals(eventService.add(edto1, udto1), e1);
        edto1.setRecipientUserId(u1);
        e1.setRecipientUserId(u1);
        assertEquals(eventService.add(edto1, udto1), e1);
        Mockito.verify(eventRepository, Mockito.times(2)).save(any(Event.class));
    }

    @Test
    void addRecurring() {

        events = new ArrayList<>();
        events.add(e1);

        e2 = e1;
        LocalDateTime start = LocalDateTime.parse(edto1.getEventStart()).plusDays(7);
        LocalDateTime end = LocalDateTime.parse(edto1.getEventStart()).plusDays(7);
        e2.setEventStart(Timestamp.valueOf(start));
        e2.setEventEnd(Timestamp.valueOf(end));
        edto2 = edto1;
        edto2.setEventStart("2020-03-26T09:00");
        edto2.setEventEnd("2020-03-26T11:00");
        events.add(e2);
        events.get(0).setRecipientUserId(u1);
        events.get(1).setRecipientUserId(u1);
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(edto1);
        System.out.println(edto2);
        System.out.println(events);
        Mockito.doReturn(e1).when(eventService).add(edto1, udto1);
        Mockito.doReturn(e2).when(eventService).add(edto2, udto1);
        assertEquals(eventService.addRecurring(edto1, udto1), events);
    }

    @Test
    void getEventsForUser() {

    }

    @Test
    void getByEventId() {
    }

    @Test
    void changeTimestapToString() {
    }

    @Test
    void updateEvent() {
    }

    @Test
    void cancelEvent() {
    }

    @Test
    void convertStringToTimeStamp() {
    }

    @Test
    void calulateFreeTimeForTheWeek() {
    }

    @Test
    void acceptEvent() {
    }

    @Test
    void listEventsNearTimeEventUsers() {
    }

    @Test
    void checkUpcomingEvents() {
    }

    @Test
    void notifyUsersOfUpcomingEvents() {
    }
}