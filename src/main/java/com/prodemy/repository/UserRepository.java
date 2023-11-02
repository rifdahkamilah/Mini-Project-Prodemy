package com.prodemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prodemy.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByEmail(String email);
}
