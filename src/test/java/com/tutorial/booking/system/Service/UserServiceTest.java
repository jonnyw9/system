/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.UserRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

    private static User u1;
    private static User u2;
    private static UserDto udto1;
    private static UserDto udto2;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService = new UserServiceImpl();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init(){
        Staff s1 = new Staff(1000000, "testRoom");
        Staff s2 = new Staff(100000, "testRoom2");
        u1 = new User(10000, "test@test.co.uk", "test", "testl", new Password(),
                true, new Roles(), new Calendar(), s1, new Student());
        u2 = new User(10001, "test@test.com", "test1", "testl1", new Password(),
                true, new Roles(), new Calendar(), s2, new Student());
        //udto1 = new UserDto(u1);
        //udto2 = new UserDto(u2);
    }


    @Test
    void getUserByUserName() {
        Mockito.when(userRepository.findByEmail("test@test.co.uk")).thenReturn(Optional.of(u1));
        assertEquals(userRepository.findByEmail("test@test.co.uk"), Optional.of(u1));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("test@test.co.uk");
    }

    @Test
    void getUserByEmail() {
        Mockito.when(userRepository.findByEmail("test@test.co.uk")).thenReturn(Optional.of(u1));
        assertEquals(userRepository.findByEmail("test@test.co.uk"), Optional.of(u1));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("test@test.co.uk");
    }

    @Test
    void getUserById() {
        Mockito.when(userRepository.findByUserId(10000)).thenReturn(Optional.of(u1));
        assertEquals(userRepository.findByUserId(10000), Optional.of(u1));
        assertNotNull(userRepository.findByUserId(10000));
        Mockito.verify(userRepository, Mockito.atLeast(1)).findByUserId(10000);
    }

    @Test
    void saveNewUser() {
    }

    @Test
    void updateDetails() {
    }

    @Test
    void deleteUserAccount() {
    }

    @Test
    void listUserByName() {
    }

    @Test
    void parseFormTime() {
    }

    @Test
    void makeUserDto() {
    }
}