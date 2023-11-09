package com.prodemy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prodemy.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query(value = "SELECT * FROM cart WHERE product_id = :product_id", nativeQuery = true)
    public List<Cart> getCartByProductId(@Param("product_id") long id);

    @Query(value = "SELECT * FROM cart WHERE user_id = :user_id", nativeQuery = true)
    public List<Cart> getCartByUserId(@Param("user_id") long id);
}
