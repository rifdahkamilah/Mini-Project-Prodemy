package com.prodemy.services;

import com.prodemy.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(long id);

    void addToCart(long id);

    //filter by name

//    List<Product> filterProductsByName(String productName);

    List<Product> findByKeyword(String keyword);

//    List<Product> saveProductToDB(MultipartFile file, String productName, String productDescription, double productPrice);
    public List<Product> findProductByPriceRange(long minPrice, long maxPrice);
    public List<Product> findProductsByPriceRangeAndName(long minPrice, long maxPrice, String name);

//    Page<Product> findPaginated(int pageNo, int pageSize);

    void saveProduct(Product product);

    void deleteProductById(long id);
}
