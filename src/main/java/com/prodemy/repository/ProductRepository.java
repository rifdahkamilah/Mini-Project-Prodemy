package com.prodemy.repository;

import com.prodemy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // List<Product> findByProductNameContaining(String productName);

    @Query(value = "select * from products where product_name like %:keyword%", nativeQuery = true)
    public List<Product> findByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM products WHERE product_price BETWEEN :minPrice AND :maxPrice", nativeQuery = true)
    public List<Product> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

    @Query(value = "SELECT * FROM Products p WHERE p.product_price BETWEEN :minPrice AND :maxPrice AND lower(p.product_name) LIKE lower(concat('%', :name, '%'))", nativeQuery = true)
    public List<Product> findProductsByPriceRangeAndName(double minPrice, double maxPrice, String name);

}
