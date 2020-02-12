package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.UserRepository;
import com.tutorial.booking.system.dao.UserDao;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserDao getUserByUserName(String email){
        Optional<User> user = userRepository.findByEmail(email);

        return user.map(UserDao::new).get();
    }

    public User getUserById(int id){

        Optional<User> user = userRepository.findByUserId(id);

        return user.orElse(null);

    }
}