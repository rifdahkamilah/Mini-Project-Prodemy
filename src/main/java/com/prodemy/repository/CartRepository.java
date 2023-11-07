package com.prodemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodemy.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
}
