package com.prodemy.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.prodemy.entity.User;
import com.prodemy.model.AdminRoleEditRequest;
import com.prodemy.model.RequestEditUser;
import com.prodemy.model.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService {
    public User save(UserDto registrationDto);

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public void editUser(String email, RequestEditUser userDto);

    public UserDto getCurrentUser(String email);

    List<User> getAllUsers(String email);

    User getUserById(long id);

    void removeUserById(long id);

    public void editUserByAdmin(AdminRoleEditRequest request);

}
