/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Repository;

import com.tutorial.booking.system.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * <p>A repository for the user table on the database. All database requests for that table go through this class.</p>
 * @author Jonathan Watt
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * <p>Finds a user by the email provided.</p>
     * @param email - The email for the potential user.
     * @return - The optional of user.
     */
    Optional<User> findByEmail(String email);

    /**
     * <p>Finds a user by an id given.</p>
     * @param userid - The id of the user given.
     * @return - The optional of the user.
     */
    Optional<User> findByUserId(int userid);

    Iterable<User> findByLastName(String lastname);

    List<User> findAllByStaffIdIsNotNull();

    /**
     * <p>Finds a staff member by search criteria passed in.</p>
     * @param name - The string to be search against the user table for a staff member.
     * @return - The list of staff members found.
     */
    @Query("select u from User u where   u.lastName like %?1% and u.staffId is not null or u.firstName like %?1% and u.staffId is not null or concat(u.firstName, ' ', u.lastName)  like %?1% and u.staffId is not null")
    List<User> findStaffByName(String name);

}
