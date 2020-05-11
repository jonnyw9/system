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

    @Mock
    UserService userService = new UserServiceImpl();

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
        Mockito.doReturn(e1).when(eventService).add(edto1, udto1);
        Mockito.doReturn(e2).when(eventService).add(edto2, udto1);
        assertEquals(eventService.addRecurring(edto1, udto1), events);
    }

    @Test
    void getEventsForUser() {
        //2x Event which is both created by and recieved by the user.
        //Event which user is creator but not receiver.
        //Event which user is reciever but not creator
        events = new ArrayList<>();
        events.add(e1);
        Event ev2 = new Event();
        ev2.setEventId(2);
        ev2.setCreatorUserId(u1);
        ev2.setRecipientUserId(u1);
        ev2.setTitle("test");
        ev2.setDescription("dff");
        ev2.setLocation("222");
        ev2.setAccepted(true);
        ev2.setEventEnd(t1);
        ev2.setEventStart(t2);
        events.add(ev2);
        System.out.println(events.get(0).toString()+ "::::" + events.get(1).toString());
        List<Event> eventsReceived = new ArrayList<>();
        eventsReceived.add(ev2);
        Event ev3 = new Event();
        ev3.setCreatorUserId(u2);
        ev3.setRecipientUserId(u1);
        ev3.setEventId(3);
        ev3.setTitle("test");
        ev3.setDescription("dff");
        ev3.setLocation("222");
        ev3.setAccepted(true);
        ev3.setEventEnd(t1);
        ev3.setEventStart(t2);
        eventsReceived.add(ev3);
        System.out.println(eventsReceived.get(0).toString() + ";;;;;" + eventsReceived.get(1).toString());
        Mockito.when(userService.getUserById(10000)).thenReturn(u1);
        Mockito.when(eventRepository.findByCreatorUserId(u1)).thenReturn(events);
        Mockito.when(eventRepository.findByRecipientUserId(u1)).thenReturn(eventsReceived);

        assertEquals(eventService.getEventsForUser(10000).size(), 3);
        Mockito.verify(eventRepository, Mockito.times(1)).findByCreatorUserId(u1);
        Mockito.verify(eventRepository, Mockito.times(1)).findByRecipientUserId(u1);
    }

    @Test
    void changeTimestapToString() {
        assertEquals(eventService.changeTimestampToString(t1), "2020-03-19T09:00");
        assertEquals(eventService.changeTimestampToString(t2),"2020-03-19T11:00");
    }

    @Test
    void convertStringToTimeStamp() {
        ArrayList<Timestamp> timestamps = new ArrayList<>();
        timestamps.add(t1);
        timestamps.add(t2);
        assertEquals(eventService.convertStringToTimeStamp(edto1), timestamps);
    }

    @Test
    void listEventsNearTimeEventUsers() {
        events = new ArrayList<>();
        events.add(e1);
        Event eve2 = new Event();
        eve2.setEventId(2);
        eve2.setCreatorUserId(u1);
        eve2.setRecipientUserId(u1);
        eve2.setTitle("test");
        eve2.setDescription("dff");
        eve2.setLocation("222");
        eve2.setAccepted(true);
        eve2.setEventEnd(Timestamp.valueOf(t1.toLocalDateTime().plusMinutes(60)));
        eve2.setEventStart(Timestamp.valueOf(t2.toLocalDateTime().plusMinutes(60)));
        events.add(eve2);
        Event eve3 = new Event();
        eve3.setCreatorUserId(u2);
        eve3.setRecipientUserId(u1);
        eve3.setEventId(3);
        eve3.setTitle("test");
        eve3.setDescription("dff");
        eve3.setLocation("222");
        eve3.setAccepted(true);
        eve3.setEventEnd(Timestamp.valueOf(t1.toLocalDateTime().plusMinutes(180)));
        eve3.setEventStart(Timestamp.valueOf(t2.toLocalDateTime().plusMinutes(180)));
        events.add(eve3);
        List<Event> eventsCreator = new ArrayList<>();
        eventsCreator.add(eve3);
        Event eve4 = new Event();
        eve4.setCreatorUserId(u2);
        eve4.setRecipientUserId(u1);
        eve4.setEventId(3);
        eve4.setTitle("test");
        eve4.setDescription("dff");
        eve4.setLocation("222");
        eve4.setAccepted(true);
        eve4.setEventEnd(Timestamp.valueOf(t1.toLocalDateTime().minusMinutes(180)));
        eve4.setEventStart(Timestamp.valueOf(t2.toLocalDateTime().minusMinutes(180)));
        eventsCreator.add(eve4);
        Mockito.doReturn(events).when(eventService).getEventsForUser(edto1.getCreatorUserId().getUserId());
        Mockito.doReturn(eventsCreator).when(eventService).getEventsForUser(edto1.getRecipientUserId().getUserId());
        assertEquals(eventService.listEventsNearTimeEventUsers(edto1), true);
        events.remove(e1);
        assertEquals(eventService.listEventsNearTimeEventUsers(edto1), false);
        events.remove(eve2);
        assertEquals(eventService.listEventsNearTimeEventUsers(edto1), false);
        Mockito.verify(eventService, Mockito.times(6)).getEventsForUser(any(Integer.class));
    }

}