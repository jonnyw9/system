/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;


/**
 * <p>Handles request for the user entity, such as getting certain users, updating their details, and deleting them.
 * Some methods return a Data Transfer Object (DTO), intended for use during the runtime of the system, See the UserDTO
 * class {@link com.tutorial.booking.system.dto.UserDto} for more details.
 * This class also acts as a buffer between the repository and the controllers so that certain pieces of logic can be
 * performed.</p>
 *
 * @Author Jonathan Watt
 * @Version 1.0.0
 */
@Service
public interface UserService {

    /**
     * <p>Simple method which creates a UserDto {@link com.tutorial.booking.system.dto.UserDto} from an email provided.
     * It gets the user from the database via the UserRepository {@Link com.tutorial.booking.system.repository.UserRepository}</p>
     * @param email the email of the user to get from the repository
     * @return a userDto created from the user return from the repository
     */
    UserDto getUserByUserName(String email);

    /**
     * <p>Simple method which returns a user from the repository from an email provided, if a user is present.</p>
     * @param email the email of the user to get from the repository
     * @return null if there is no user found. The User entity if there is one found.
     */
    User getUserByEmail(String email);

    /**
     * <p>Simple method to get a user from the repository from a userId provided.</p>
     * @param id of the you wished to be found.
     * @return null if there is no user found. The User entity if there is one found.
     */
    User getUserById(int id);

    /**
     * <p>A method which takes a UserDto {@link com.tutorial.booking.system.dto.UserDto} and creates a new User entity
     * based on the details in the UserDto provided.</p>
     * @param userDto the Validated UserDto with the details of the user which is about the be created
     */
    void saveNewUser(UserDto userDto);


    void updateDetails(UserDto userDto, String edit);


    void deleteUserAccount(int id);


    List<User> listUserByName(String name);


    Time parseFormTime(String time);


    UserDto makeUserDto(Authentication authentication);
}
