package com.prodemy.repository;

import com.prodemy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByProductNameContaining(String productName);

    @Query(value = "select e from Product e where e.productName like %:keyword%", nativeQuery = true)
    List<Product> findByKeyword(@Param("keyword") String keyword);

}
