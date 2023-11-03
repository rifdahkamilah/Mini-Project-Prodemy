package com.prodemy.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.prodemy.entity.UserEntity;
import com.prodemy.model.RequestEditUser;
import com.prodemy.model.UserDto;

public interface UserService extends UserDetailsService {
    public UserEntity save(UserDto registrationDto);
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    public void editUser(String email, RequestEditUser userDto);
    public UserDto getCurrentUser(String email);
}
