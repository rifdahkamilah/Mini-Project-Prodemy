package com.prodemy.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prodemy.entity.Role;
import com.prodemy.entity.User;
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

        User user = new User(registrationDto.getName(), registrationDto.getEmail(), registrationDto.getPassword(),
                role);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto req = userRepository.findByEmail(email);
        if (req == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        validator.validate(req);

        User user = new User(req.getEmail(), req.getName(), req.getPassword(), req.getRole());
        return (UserDetails) user;

    }

}
