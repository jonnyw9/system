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
     * It gets the user from the database via the UserRepository {@Link com.tutorial.booking.system.repository.UserRepository}.
     * This method is used for the purposes of authentication during the runtime of the system.</p>
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
     * @return the user which has been created. For testing purposes only
     */
    User saveNewUser(UserDto userDto);

    /**
     * <p>A method which takes a UserDto {@link com.tutorial.booking.system.dto.UserDto} with some new information on
     * and saves it to the database. It will take the type of edit which is to be made and edit that particular field.</p>
     * @param userDto The data transfer object with the specific changed on it.
     * @param edit The type of edit the user wished to do.
     * @return the user which has been saved. For testing purposes only.
     */
    User updateDetails(UserDto userDto, String edit);

    /**
     * <p>A method which deletes the user specified and all the entities which are linked to that user.</p>
     * @param id the ID of the user wished to be deleted.
     */
    void deleteUserAccount(int id);


    /**
     * <p>List a number of users based on a string. Used in the search staff function</p>
     * @param name the name of the staff member the user wishes to get.
     * @return The list return by the repository.
     */
    List<User> listUserByName(String name);

    /**
     * <p>Parses the time from the user dto into a java.sql.time object to be stored in the database</p>
     * @param time the time that is wished to be parsed.
     * @return the Time object that has been made from the String passed in
     */
    Time parseFormTime(String time);

    /**
     * <p>Creates a UserDto {@link com.tutorial.booking.system.dto.UserDto} from the authenticated user using the
     * system.</p>
     * @param authentication the authenticated user.
     * @return The userDto for that user
     */
    UserDto makeUserDto(Authentication authentication);
}
