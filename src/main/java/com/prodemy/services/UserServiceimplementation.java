package com.prodemy.services;

import java.util.*;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodemy.entity.Role;
import com.prodemy.entity.User;
import com.prodemy.model.RequestEditUser;
import com.prodemy.model.UserDto;
import com.prodemy.repository.RoleRepository;
import com.prodemy.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImplementation implements UserService {

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
    public User save(UserDto registrationDto) {
        validator.validate(registrationDto);
        Role role = roleRepository.findById(1L).orElse(null);

        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setName(registrationDto.getName());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRoles(Arrays.asList(role));

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User req = userRepository.findByEmail(email);
        if (req == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        validator.validate(req);
        log.info("request {} ", req);

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                req.getEmail(), req.getPassword(),
                getAuthorities(req.getRoles()));
        return user;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void editUser(String email, RequestEditUser req) {
        validator.validate(req);

        User user = userRepository.findByEmail(email);

        if (Objects.nonNull(req.getEmail())) {
            user.setEmail(req.getEmail());
        }

        if (req.getPassword() != "") {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        if (Objects.nonNull(req.getName())) {
            user.setName(req.getName());
        }

        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email);
        return UserDto.builder().email(user.getEmail()).name(user.getName()).password(user.getPassword()).build();
    }

    @Override
    public List<User> getAllUsers(String email) {
        List<User> user = userRepository.findAll();
        for (int i = 0; i < user.size(); i++) {
            if (user.get(i).getEmail().equals(email)) {
                user.remove(i);
            }
        }
        return user;
    }

    @Override
    public User getUserById(long id) {
        // User user = new User();
        // if (us.isPresent()) {
        // user = optional.get();
        // } else {
        // throw new RuntimeException("ID User tidak dapat ditemukan :: " + id);
        // }
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void removeUserById(long id) {
        userRepository.deleteById(id);
    }

}
