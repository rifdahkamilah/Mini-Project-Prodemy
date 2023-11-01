package com.prodemy.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prodemy.entity.Role;
import com.prodemy.entity.User;
import com.prodemy.model.RequestEditUser;
import com.prodemy.model.UserDto;
import com.prodemy.repository.RoleRepository;
import com.prodemy.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ValidationService validator;

    @Override
    public User save(UserDto registrationDto) {
        Role role = roleRepository.findById(1L).orElse(null);

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setName(registrationDto.getName());
        user.setPassword(registrationDto.getPassword());
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User req = userRepository.findByEmail(email);
        if (req == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        validator.validate(req);

        User user = new User(req.getEmail(), req.getName(), req.getPassword(), req.getRole());
        return (UserDetails) user;

    }

    @Override
    public void editUser(User user, RequestEditUser req) {
        validator.validate(req);

        if ects.nonNull(req.getEmail())){
            user.setEmail(req.getEmail());
        }

        if ects.nonNull(req.getPassword())){
            user.setPassword(req.getPassword());
        }

        if ects.nonNull(req.getName())){
            user.setName(req.getName());
        }

        userRepository.save(user);
    }

}
