package com.prodemy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prodemy.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT * FROM cart WHERE product_id = :product_id", nativeQuery = true)
    public List<Cart> getCartByProductId(@Param("product_id") long id);
}
