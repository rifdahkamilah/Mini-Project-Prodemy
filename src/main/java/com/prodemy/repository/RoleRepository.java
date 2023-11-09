package com.prodemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prodemy.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
