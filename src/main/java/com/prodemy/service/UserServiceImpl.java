package com.prodemy.service;

import com.prodemy.entity.User;
import com.prodemy.entity.Role;
import com.prodemy.repository.UserRepository;
import com.prodemy.web.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

//    public UserServiceImpl(UserRepository userRepository) {
//        super();
//        this.userRepository = userRepository;
//    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getName(), registrationDto.getEmail(), registrationDto.getPassword(), (Role) Arrays.asList(new Role("ROLE_USER")));

        return userRepository.save(user);
    }
}
