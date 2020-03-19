package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.*;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

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

    @Autowired
    CalendarRepository calendarRepository;

    @Autowired
    NotificationService notificationService;

    @Override
    public UserDto getUserByUserName(String email){
        //Get Optional User from the repository
        Optional<User> user = userRepository.findByEmail(email);

        //Return the user mapped to a userDto, if it is present.
        return user.map(UserDto::new).get();
    }

    @Override
    public User getUserByEmail(String email){
        //Get Optional User from the repository
        Optional<User> user = userRepository.findByEmail(email);

        //Return that user or null depending on its existence
        return user.orElse(null);
    }

    @Override
    public User getUserById(int id){
        //Get Optional User from the repository
        Optional<User> user = userRepository.findByUserId(id);
        //Return that user or null depending on its existence
        return user.orElse(null);
    }

    @Override
    public User saveNewUser(UserDto userDto){
        //Create a new user and add the details from the UserDto
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setActive(true);

        //Create a new password
        Password password = new Password();
        //Create a new instance of the password encoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //Set the password as the encoded version
        password.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPassword(password);

        //Create a new Roles
        Roles roles = new Roles();
        //Set the roles to whether the user is staff or student
        roles.setStudent(userDto.isStudent());
        roles.setStaff(userDto.isStaff());
        //TODO Implement admin functionality
        roles.setAdmin(false);
        //Set the user roles to the roles entity created
        user.setRoleId(roles);



        //IF the user is staff
        if(userDto.isStaff()){
            //Create a staff entity and set the details
            Staff staff = new Staff();
            staff.setRoom(userDto.getRoom());
            user.setStaffId(staff);

            //Create a new calendar entity and fill the details in
            Calendar calendar = new Calendar();
            calendar.setDayStartTime(parseFormTime(userDto.getStartTime()));
            calendar.setDayEndTime(parseFormTime(userDto.getEndTime()));
            user.setCalendarId(calendar);
        }

        //If the user is a student. Create the necessary student entity and fill in the details
        if(userDto.isStudent()){
            Student student = new Student();
            student.setStudentNumber(userDto.getStudentNumber());
            user.setStudentId(student);
        }

        //Save the user via the repository
        return userRepository.save(user);
    }

    @Override
    public User updateDetails(UserDto userDto, String edit){
        //Get the existing User by ID
        User user = getUserById(userDto.getUserId());
        //Switch for the type of edit the user wished to do.
        switch (edit) {
            case "name":
                //Do a check on the name and update the field.
                if(userDto.getFirstName() != null){
                    user.setFirstName(userDto.getFirstName());
                }
                if(userDto.getLastName() != null){
                    user.setLastName(userDto.getLastName());
                }

                break;
            case "email":
                //Checks on email and update field
                if(userDto.getEmail() != null){
                    user.setEmail(userDto.getEmail());
                }

                break;
            case "password":
                //Bit of validation on the password
                if(userDto.getPassword() != null && userDto.getConfirmPassword() != null
                        && userDto.getPassword().equals(userDto.getConfirmPassword())){
                    //Get the password entity from the database
                    Password password = passwordRepository.getOne(user.getPassword().getPasswordId());

                    //Encode the password
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    password.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    //Save the password
                    passwordRepository.save(password);
                    //Notify the user
                    notificationService.passwordChanged(user);
                }
                break;
            case "room":
                //Get a staff member
                Staff staff = staffRepository.getOne(user.getStaffId().getStaffId());
                //Update the room and save it.
                staff.setRoom(userDto.getRoom());
                staffRepository.save(staff);
                break;
            case "times":
                Calendar calendar = calendarRepository.getOne(user.getCalendarId().getCalendarId());
                calendar.setDayStartTime(parseFormTime(userDto.getStartTime()));
                calendar.setDayEndTime(parseFormTime(userDto.getEndTime()));
                calendarRepository.save(calendar);
                break;
        }
        //Save the updated user to the repository and return it
        return userRepository.save(user);
    }

    @Override
    public void deleteUserAccount(int id){

        User user = userRepository.getOne(id);

        UserDto userDto = new UserDto(user);

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
            Calendar calendar = calendarRepository.getOne(user.getCalendarId().getCalendarId());
            calendarRepository.delete(calendar);
        }
        if(user.getStudentId() != null){
            studentRepository.delete(user.getStudentId());
        }


        //Get and delete all Notifications
        List<Notification> notifications = notificationService.getUserNotifications(user.getUserId());

        for(Notification notification: notifications){
            notificationService.deleteNotification(notification);
        }

        //Finally delete the user
        userRepository.delete(user);
    }

    @Override
    public List<User> listUserByName(String name){
        //Return what is found from the repository.
        return userRepository.findStaffByName(name);
    }

    @Override
    public Time parseFormTime(String time){
        //Makes a time object
        Time time1 = null;
        try{
            //Parses that String into the time object
            time1 = Time.valueOf(time + ":00");
        }catch(Exception e){
            e.printStackTrace();
        }
        //Returns it
        return time1;
    }

    @Override
    public UserDto makeUserDto(Authentication authentication){
        System.out.println(authentication.getName());
        return getUserByUserName(authentication.getName());
    }
}
