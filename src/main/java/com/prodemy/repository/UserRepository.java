package com.prodemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prodemy.entity.User;
import com.prodemy.model.UserDto;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public UserDto findByEmail(String email);
}
