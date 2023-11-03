package com.prodemy.services;

import java.util.*;

import java.util.stream.Collectors;

import com.prodemy.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.prodemy.entity.Role;
import org.springframework.security.core.userdetails.User;
import com.prodemy.entity.UserEntity;
import com.prodemy.model.RequestEditUser;
import com.prodemy.model.UserDto;
import com.prodemy.repository.RoleRepository;
import com.prodemy.repository.UserRepository;
import com.prodemy.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceimplementation implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ValidationService validator;

    @Override
    @Transactional
    public UserEntity save(UserDto registrationDto) {
        validator.validate(registrationDto);
        Role role = roleRepository.findById(1L).orElse(null);

        UserEntity user = new UserEntity();
        user.setEmail(registrationDto.getEmail());
        user.setName(registrationDto.getName());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRoles(Arrays.asList(role));

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity req = userRepository.findByEmail(email);
        if (req == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        validator.validate(req);
        log.info("request {} ", req);

        User user = new User(req.getEmail(), req.getPassword(),
                getAuthorities(req.getRoles()));
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public void editUser(String email, RequestEditUser req) {
        validator.validate(req);

        UserEntity user = userRepository.findByEmail(email);

        if (Objects.nonNull(req.getEmail())) {
            user.setEmail(req.getEmail());
        }

        if (Objects.nonNull(req.getPassword()) && req.getPassword() != "") {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        if (Objects.nonNull(req.getName())) {
            user.setName(req.getName());
        }

        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUser(String email) {
        UserEntity user = userRepository.findByEmail(email);
        System.out.println("di service " + user.getPassword());
        return UserDto.builder().email(user.getEmail()).name(user.getName()).password(user.getPassword()).build();
    }

}
