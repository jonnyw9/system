/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Constraint;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
@SpringBootTest
class EventValidationTest {

    @InjectMocks
    @Spy
    EventValidation eventValidation = new EventValidationImpl();

    private static String time1;
    private static String time2;
    private static String time3;

    @Before
    public void setUp() { MockitoAnnotations.initMocks(this);}

    @BeforeAll
    public static void init(){
        time1 = "2020-03-19T09:00";
        time2 = "2020-03-19T09:30";
        time3 = "2020-03-19T09:12";
    }

    @Test
    void timeValidation() {
        assertTrue(eventValidation.timeValidation(time1));
        assertTrue(eventValidation.timeValidation(time2));
        assertFalse(eventValidation.timeValidation(time3));
        String test = null;
        String test2 = "";
        assertFalse(eventValidation.timeValidation(test));
        assertFalse(eventValidation.timeValidation(test2));
    }
}