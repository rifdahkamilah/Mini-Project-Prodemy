package com.prodemy.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.prodemy.entity.User;
import com.prodemy.model.UserDto;

public interface UserService extends UserDetailsService {
    public User save(UserDto registrationDto);
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
//    public User getUserById(long id);
//
//    public void saveUser(User user);
}