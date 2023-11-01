package com.prodemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodemy.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
