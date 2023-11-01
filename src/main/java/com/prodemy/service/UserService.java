package com.prodemy.service;

import com.prodemy.entity.User;
import com.prodemy.web.dto.UserRegistrationDto;

public interface UserService {
    User save(UserRegistrationDto registrationDto);
}
