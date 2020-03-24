package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(int userid);

    Iterable<User> findByLastName(String lastname);

    List<User> findAllByStaffIdIsNotNull();

    @Query("select u from User u where   u.lastName like %?1% and u.staffId is not null or u.firstName like %?1% and u.staffId is not null or concat(u.firstName, ' ', u.lastName)  like %?1% and u.staffId is not null")
    List<User> findStaffByName(String name);

}
