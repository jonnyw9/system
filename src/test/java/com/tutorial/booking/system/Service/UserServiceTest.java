/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.*;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.*;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import java.sql.Time;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
@SpringBootTest
class UserServiceTest {

    private static User u1;
    private static User u2;
    private static UserDto udto1;
    private static UserDto udto2;
    private static Student st1;
    private static Password p1;
    private static Password p2;
    private static Staff s1;
    private static Staff s2;
    private static Calendar c1;
    private static Calendar c2;
    private static Roles r1;
    private static List<Event> events;

    @InjectMocks
    UserService userService = new UserServiceImpl();

    @Mock
    NotificationService notificationService = new NotificationServiceImpl();

    @Mock
    UserRepository userRepository;

    @Mock
    CalendarRepository calendarRepository;

    @Mock
    PasswordRepository passwordRepository;

    @Mock
    StaffRepository staffRepository;

    @Mock
    EventRepository eventRepository;

    @Mock
    RolesRepository rolesRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    EventService eventService = new EventServiceImpl();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        p1 = new Password(10000, bCryptPasswordEncoder.encode("password"));
        p2 = new Password(10000, bCryptPasswordEncoder.encode("password1"));
        s1 = new Staff(1000000, "testRoom");
        s2 = new Staff(1000000, "testRoom2");
        st1 = new Student(10000, "123");
        Student st2 = new Student(10000, "1234");
        r1 = new Roles(1100, true, true, false);
        c1 = new Calendar(1000000, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"));
        c2 = new Calendar(1000000, Time.valueOf("08:00:00"), Time.valueOf("16:00:00"));
        u1 = new User(10000, "test@test.co.uk", "test", "testl", p1,
                true, r1, c1, s1, st1);
        u2 = new User(10000, "test@test.com", "test1", "testl1", p2,
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

        events = new ArrayList<>();
        events.add(new Event());

    }


    @Test
    void getUserByUserName() {
        udto2.setConfirmPassword(null);
        udto2.setPassword(null);
        Mockito.when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(u2));
        assertEquals(userService.getUserByUserName("test@test.com"), udto2);
        assertNotNull(userService.getUserByUserName("test@test.com"));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).findByEmail("test@test.com");
    }

    @Test
    void getUserByEmail() {
        Mockito.when(userRepository.findByEmail("test@test.co.uk")).thenReturn(Optional.of(u1));
        assertEquals(userService.getUserByEmail("test@test.co.uk"), u1);
        Mockito.verify(userRepository, Mockito.atLeastOnce()).findByEmail("test@test.co.uk");
    }

    @Test
    void getUserById() {
        Mockito.when(userRepository.findByUserId(10000)).thenReturn(Optional.of(u1));
        assertEquals(userService.getUserById(10000), u1);
        assertNotNull(userService.getUserById(10000));
        Mockito.verify(userRepository, Mockito.atLeastOnce()).findByUserId(10000);
    }

    @Test
    void saveNewUser() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(u1);
        udto1 = new UserDto(u1);
        udto1.setPassword("password");
        udto1.setStaff(true);
        udto1.setStudent(true);
        udto1.setStudentNumber("123");
        assertEquals(userService.saveNewUser(udto1), u1);
        udto1.setStaff(true);
        udto1.setStudent(false);
        u1.setStudentId(null);
        assertEquals(userService.saveNewUser(udto1), u1);
        udto1.setStaff(false);
        udto1.setStudent(true);
        u1.setStudentId(st1);
        userService.saveNewUser(udto1);
        Mockito.verify(userRepository, Mockito.times(3)).save(any(User.class));
    }

    @Test
    void updateDetails() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Mockito.when(userRepository.findByUserId(10000)).thenReturn(Optional.of(u1));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(u1);
        Mockito.when(passwordRepository.getOne(u1.getPassword().getPasswordId())).thenReturn(p1);
        Mockito.when(staffRepository.getOne(u1.getStaffId().getStaffId())).thenReturn(s1);
        Mockito.when(calendarRepository.getOne(u1.getCalendarId().getCalendarId())).thenReturn(c1);

        assertEquals((userService.updateDetails(udto1, "name").getFirstName()+
                userService.updateDetails(udto1, "name").getLastName()), (u1.getFirstName()+u1.getLastName()));

        assertEquals(userService.updateDetails(udto1, "email").getEmail(), u1.getEmail());


        assertTrue(bCryptPasswordEncoder.matches("password",
                userService.updateDetails(udto1, "password").getPassword().getPassword()));

        assertEquals(userService.updateDetails(udto1, "room").getStaffId().getRoom(), u1.getStaffId().getRoom());

        assertEquals((userService.updateDetails(udto1, "times").getCalendarId().getDayStartTime().toString() +
                        userService.updateDetails(udto1, "times").getCalendarId().getDayEndTime().toString()),
                (u1.getCalendarId().getDayStartTime().toString() + u1.getCalendarId().getDayEndTime().toString()));

        assertEquals((userService.updateDetails(udto2, "name").getFirstName()+
                userService.updateDetails(udto2, "name").getLastName()), (u2.getFirstName()+u2.getLastName()));

        assertEquals(userService.updateDetails(udto2, "email").getEmail(), u2.getEmail());

        udto2.setConfirmPassword("password1");
        udto2.setPassword("password1");

        assertTrue(bCryptPasswordEncoder.matches("password1",
                userService.updateDetails(udto2, "password").getPassword().getPassword()));

        assertEquals(userService.updateDetails(udto2, "room").getStaffId().getRoom(), u2.getStaffId().getRoom());

        assertEquals((userService.updateDetails(udto2, "times").getCalendarId().getDayStartTime().toString() +
                        userService.updateDetails(udto2, "times").getCalendarId().getDayEndTime().toString()),
                (u2.getCalendarId().getDayStartTime().toString() + u2.getCalendarId().getDayEndTime().toString()));

        Mockito.verify(userRepository, Mockito.times(14)).save(any(User.class));
    }

    @Test
    void deleteUserAccount() {
        Mockito.when(userRepository.getOne(10000)).thenReturn(u1);
        Mockito.when(passwordRepository.getOne(u1.getPassword().getPasswordId())).thenReturn(p1);
        Mockito.when(calendarRepository.getOne(u1.getCalendarId().getCalendarId())).thenReturn(c1);
        Mockito.when(eventRepository.findByCreatorUserId(u1)).thenReturn(events);
        Mockito.when(eventRepository.findByRecipientUserId(u1)).thenReturn(events);
        Mockito.when(rolesRepository.getOne(u1.getRoleId().getRoleId())).thenReturn(r1);
        userService.deleteUserAccount(10000);
        Mockito.verify(userRepository, Mockito.times(1)).delete(u1);
        Mockito.verify(passwordRepository, Mockito.times(1)).delete(p1);
        Mockito.verify(staffRepository, Mockito.times(1)).delete(s1);
        Mockito.verify(calendarRepository, Mockito.times(1)).delete(c1);
        Mockito.verify(studentRepository, Mockito.times(1)).delete(st1);
        Mockito.verify(eventRepository, Mockito.atLeast(0)).delete(any(Event.class));
    }

    @Test
    void listUserByName() {
        List<User> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        Mockito.when(userRepository.findStaffByName("_")).thenReturn(users);
        assertEquals(userService.listUserByName("_"), users);
        users.remove(u2);
        Mockito.when(userRepository.findStaffByName("test")).thenReturn(users);
        assertEquals(userService.listUserByName("test"), users);
        Mockito.verify(userRepository, Mockito.times(2)).findStaffByName(any(String.class));
    }

    @Test
    void parseFormTime() {
        Time time = c1.getDayStartTime();
        Time time1 = c1.getDayEndTime();
        assertEquals(userService.parseFormTime("09:00"), time);
        assertEquals(userService.parseFormTime("17:00"), time1);
    }
}