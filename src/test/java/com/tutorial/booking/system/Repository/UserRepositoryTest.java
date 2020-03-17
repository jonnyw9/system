/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.SystemApplication;
import com.tutorial.booking.system.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = SystemApplication.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        //Given
        User test1 = new User();
        test1.setEmail("testemail@test.com");
        User test2 = new User();
        test2.setEmail("test@test.com");
        userRepository.save(test1);
        userRepository.save(test2);

        //When
        Optional<User> found1 = userRepository.findByEmail(test1.getEmail());
        Optional<User> found2 = userRepository.findByEmail(test2.getEmail());

        User foundUser1 = found1.orElse(null);
        User foundUser2 = found2.orElse(null);

        //Then
        assert foundUser1 != null;
        assertEquals(test1.getEmail(), foundUser1.getEmail());

        assert foundUser2 != null;
        assertEquals(test2.getEmail(), foundUser2.getEmail());
    }

    @Test
    void findByUserId() {
    }

    @Test
    void findByLastName() {
    }

    @Test
    void findAllByStaffIdIsNotNull() {
    }

    @Test
    void findStaffByName() {
    }
}