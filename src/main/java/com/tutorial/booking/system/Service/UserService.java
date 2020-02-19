package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.UserRepository;
import com.tutorial.booking.system.dto.UserDto;
import com.tutorial.booking.system.model.Password;
import com.tutorial.booking.system.model.Roles;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

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

    public void save(UserDto userDto){
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

        userRepository.save(user);
    }

    public UserDto makeUserDto(Authentication authentication){
        System.out.println(authentication.getName());
        UserDto user = getUserByUserName(authentication.getName());
        return user;
    }
}
