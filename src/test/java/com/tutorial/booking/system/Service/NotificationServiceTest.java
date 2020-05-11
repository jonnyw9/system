/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.NotificationRepository;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.*;
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

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
@SpringBootTest
class NotificationServiceTest {

    private static User u1;
    private static User u2;
    private static UserDto udto1;
    private static UserDto udto2;
    private static Password p1;
    private static Password p2;
    private static Staff s1;
    private static Staff s2;
    private static Calendar c1;
    private static Student st1;
    private static Student st2;
    private static Calendar c2;
    private static Roles r1;
    private static Event e1;
    private static Event e2;
    private static Notification n1;
    private static Notification n2;
    private static Notification n3;
    private static Timestamp t1;
    private static Timestamp t2;
    private static List<Notification> notifications;

    @InjectMocks
    @Spy
    NotificationService notificationService = new NotificationServiceImpl();

    @Mock
    UserService userService = new UserServiceImpl();

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    EmailSender emailSender;

    @Before
    public void setup(){MockitoAnnotations.initMocks(this);}

    @BeforeAll
    public static void init(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        p1 = new Password(10000, bCryptPasswordEncoder.encode("password"));
        p2 = new Password(100001, bCryptPasswordEncoder.encode("password1"));
        s1 = new Staff(1000000, "testRoom");
        s2 = new Staff(10000001, "testRoom2");
        st1 = new Student(10000, "123");
        st2 = new Student(100001, "1234");
        r1 = new Roles(1100, true, true, false);
        c1 = new Calendar(1000000, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"));
        c2 = new Calendar(10000001, Time.valueOf("08:00:00"), Time.valueOf("16:00:00"));
        u1 = new User(10000, "test@test.co.uk", "test", "testl", p1,
                true, r1, c1, s1, st1);
        u2 = new User(100001, "test@test.com", "test1", "testl1", p2,
                true, new Roles(), c2, s2, st2);
        udto1 = new UserDto(u1);
        udto1.setPassword("password");
        udto1.setConfirmPassword("password");
        udto1.setStaff(true);
        udto1.setStudent(true);
        udto1.setStudentNumber("123");
        udto2 = new UserDto(u2);
        udto2.setPassword("password1");
        udto2.setConfirmPassword("password1");
        t1 = Timestamp.valueOf(("2020-03-19T09:00"+":00").replace("T", " "));
        t2 = Timestamp.valueOf(("2020-03-19T11:00"+":00").replace("T", " "));
        e1 = new Event(1, t1, t2, "test", "test2", u1, u2, "location");
        e2 = new Event(1, t1, t2, "test", "test2", u1, u1, "location");
        n1 = new Notification();
        n1.setTitle("Event Cancelled");
        n1.setDescription("Your event '"+ e1.getTitle() + "' at: " +
                e1.getEventStart().toLocalDateTime().toString().replace("T", " ") + " has been cancelled.");
        n1.setUserId(u1);
        n1.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        n2 = new Notification();
        n2.setTitle("Event Cancelled");
        n2.setDescription("Your event '"+ e1.getTitle() + "' at: " +
                e1.getEventStart().toLocalDateTime().toString().replace("T", " ") + " has been cancelled.");
        n2.setUserId(u2);
        n2.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        n3 = new Notification();
        n3.setTitle("Event Cancelled");
        n3.setDescription("Your event '"+ e1.getTitle() + "' at: " +
                e1.getEventStart().toLocalDateTime().toString().replace("T", " ") + " has been cancelled.");
        n3.setUserId(u2);
        n3.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        n3.setSeen(true);
        notifications = new ArrayList<>();
        notifications.add(n1);
        notifications.add(n2);
    }

    @Test
    void eventCancelled() {
        Mockito.when(notificationRepository.save(any(Notification.class))).thenReturn(n1);
        Mockito.when(userService.getUserById(10000)).thenReturn(u1);
        Mockito.when(userService.getUserById(100001)).thenReturn(u2);
        assertEquals(notificationService.eventCancelled(e1).get(0), notifications.get(0));
        assertEquals(notificationService.eventCancelled(e1).get(1), notifications.get(1));
        assertEquals(notificationService.eventCancelled(e2).get(0), n1);
    }

    @Test
    void timeParse() {
        assertEquals(notificationService.timeParse(t1.toString()), t1.toString().replace('T', ' '));
    }

    @Test
    void noUnreadNotifications() {
        Mockito.doReturn(notifications).when(notificationService).getUserNotifications(10000);
        assertEquals(notificationService.noUnreadNotifications(udto1), 2);
        notifications.add(n3);
        assertEquals(notificationService.noUnreadNotifications(udto1), 2);
        notifications.remove(n3);
    }
}