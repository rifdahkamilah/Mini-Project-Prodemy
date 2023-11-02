package com.prodemy.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prodemy.entity.Role;
import org.springframework.security.core.userdetails.User;
import com.prodemy.entity.UserEntity;
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

        User user = new User(req.getEmail(), req.getPassword(),
                getAuthorities(req.getRoles()));
        return user;

    }

    // public Collection<? extends GrantedAuthority> getRoles() {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // return authentication.getAuthorities();
    // }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public void editUser(UserEntity user, RequestEditUser req) {
        validator.validate(req);

        if (Objects.nonNull(req.getEmail())) {
            user.setEmail(req.getEmail());
        }

        if (Objects.nonNull(req.getPassword())) {
            user.setPassword(req.getPassword());
        }

        if (Objects.nonNull(req.getName())) {
            user.setName(req.getName());
        }

        userRepository.save(user);
    }

}
