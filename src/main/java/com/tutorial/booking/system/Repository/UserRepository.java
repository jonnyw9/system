package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(int userid);

    Iterable<User> findByLastName(String lastname);
}
