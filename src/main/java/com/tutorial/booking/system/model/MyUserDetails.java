/*
 * Copyright (c) 2020. To JWIndustries
 */

package com.tutorial.booking.system.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * <p>A class which holds all the information for an authenticated user on the system.</p>
 * @author Jonathan Watt
 */
public class MyUserDetails implements UserDetails {

    private String email;
    private Password password;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();
        List<GrantedAuthority> roles = new ArrayList<>();
        if(user.getRoleId().isStaff()){
            System.out.println("1");
            roles.add(new SimpleGrantedAuthority("ROLE_STAFF"));
        }
        if(user.getRoleId().isStudent()){
            roles.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
            System.out.println("2");
        }
        if(user.getRoleId().isAdmin()){
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            System.out.println("3");
        }
        this.authorities = roles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password.getPassword();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
