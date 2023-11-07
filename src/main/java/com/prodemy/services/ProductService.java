package com.prodemy.services;

import com.prodemy.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(long id);

    void addToCart(long id, String email);

    List<Product> findByKeyword(String keyword);

    List<Product> findProductByPriceRange(long minPrice, long maxPrice);

    List<Product> findProductsByPriceRangeAndName(long minPrice, long maxPrice, String name);

    // void saveProduct(Product product);
    //
    // void deleteProductById(long id);

    void addProduct(Product product);

    void removeProductById(long id);

}
