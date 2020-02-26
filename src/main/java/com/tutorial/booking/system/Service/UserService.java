package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.*;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    RolesRepository rolesRepository;

    public UserDto getUserByUserName(String email){
        Optional<User> user = userRepository.findByEmail(email);

        return user.map(UserDto::new).get();
    }

    public User getUserByEmail(String email){

        Optional<User> user = userRepository.findByEmail(email);

        return user.orElse(null);
    }

    public User getUserById(int id){

        Optional<User> user = userRepository.findByUserId(id);

        return user.orElse(null);
    }

    public void saveNewUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setActive(true);

        Password password = new Password();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPassword(password);

        Roles roles = new Roles();
        roles.setStudent(userDto.isStudent());
        roles.setStaff(userDto.isStaff());
        roles.setAdmin(false);
        user.setRoleId(roles);

        if(userDto.isStaff()){
            Staff staff = new Staff();
            staff.setRoom(userDto.getRoom());
            user.setStaffId(staff);
        }

        if(userDto.isStudent()){
            Student student = new Student();
            student.setStudentNumber(userDto.getStudentNumber());
            user.setStudentId(student);
        }

        userRepository.save(user);
    }

    public void updateDetails(UserDto userDto, String edit){
        User user = getUserById(userDto.getUserId());

        switch (edit) {
            case "name":
                if(userDto.getFirstName() != null){
                    user.setFirstName(userDto.getFirstName());
                }
                if(userDto.getLastName() != null){
                    user.setLastName(userDto.getLastName());
                }

                userRepository.save(user);

                break;
            case "email":
                if(userDto.getEmail() != null){
                    user.setEmail(userDto.getEmail());
                }

                userRepository.save(user);

                break;
            case "password":
                if(userDto.getPassword() != null && userDto.getConfirmPassword() != null
                        && userDto.getPassword().equals(userDto.getConfirmPassword())){
                    Password password = passwordRepository.getOne(user.getPassword().getPasswordId());

                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    password.setPassword(passwordEncoder.encode(userDto.getPassword()));

                    passwordRepository.save(password);
                }

                break;
        }

        userRepository.save(user);
    }

    public void deleteUserAccount(int id){

        User user = userRepository.getOne(id);

        //Cancel event pertaining to the user

        List<Event> createdEvents = eventRepository.findByCreatorUserId(user);

        if(!createdEvents.isEmpty()){
            for(Event event : createdEvents){
                eventService.cancelEvent(event.getEventId());
                //Send a notification to other affected users
            }
        }

        List<Event> recievedEvents = eventRepository.findByRecipientUserId(user);

        if(!recievedEvents.isEmpty()){
            for (Event event: recievedEvents) {
                eventService.cancelEvent(event.getEventId());
                //Send a notification to other affected users
            }
        }

        //Delete the password linked to the account
        Password password = passwordRepository.getOne(user.getPassword().getPasswordId());
        passwordRepository.delete(password);

        //Delete the roles linked to the account
        Roles roles = rolesRepository.getOne(user.getRoleId().getRoleId());
        rolesRepository.delete(roles);

        //Delete staff and student entities
        if(user.getStaffId() != null){
            staffRepository.delete(user.getStaffId());
        }
        if(user.getStudentId() != null){
            studentRepository.delete(user.getStudentId());
        }

        //Finally delete the user
        userRepository.delete(user);
    }

    public Iterable<User> listUserByLastName(String lastname){
        return userRepository.findByLastName(lastname);
    }

    public UserDto makeUserDto(Authentication authentication){
        System.out.println(authentication.getName());
        return getUserByUserName(authentication.getName());
    }
}
