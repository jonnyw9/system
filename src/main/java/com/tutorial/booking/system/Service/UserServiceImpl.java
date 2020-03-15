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
    EventServiceImpl eventService;

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
    NotificationServiceImpl notificationService;

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
    public void saveNewUser(UserDto userDto){
        //Create a new user and add the details from the UserDto
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

            Calendar calendar = new Calendar();
            calendar.setDayStartTime(parseFormTime(userDto.getStartTime()));
            calendar.setDayEndTime(parseFormTime(userDto.getEndTime()));
            user.setCalendarId(calendar);
        }

        if(userDto.isStudent()){
            Student student = new Student();
            student.setStudentNumber(userDto.getStudentNumber());
            user.setStudentId(student);
        }

        userRepository.save(user);

        notificationService.accountCreated(user);
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
                    notificationService.passwordChanged(user);
                }
                break;
        }

        userRepository.save(user);
    }

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



        //Finally delete the user
        userRepository.delete(user);
    }

    public List<User> listUserByName(String name){
        return userRepository.findStaffByName(name);
    }

    public Time parseFormTime(String time){
        System.out.println(time);
        Time time1 = null;
        try{
            time1 = Time.valueOf(time + ":00");
        }catch(Exception e){
            e.printStackTrace();
        }
        return time1;
    }

    public UserDto makeUserDto(Authentication authentication){
        System.out.println(authentication.getName());
        return getUserByUserName(authentication.getName());
    }
}
