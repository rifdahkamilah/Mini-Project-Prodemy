package com.prodemy.services;

import com.prodemy.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    void addToCart(Long id);

    //filter by name

//    List<Product> filterProductsByName(String productName);

    List<Product> findByKeyword(String keyword);
}
