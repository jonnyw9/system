/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.Service;

import com.tutorial.booking.system.Repository.PasswordRepository;
import com.tutorial.booking.system.Repository.UserRepository;
import com.tutorial.booking.system.model.MyUserDetails;
import com.tutorial.booking.system.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>Handles the getting of the username, or in the case the email, for the purposes of the spring security
 * functionality</p>
 * @author Jonathan Watt
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        //In this case the email is the username
        Optional<User> user = userRepository.findByEmail(email);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: "+ email));

        return user.map(MyUserDetails::new).get();
    }
}
