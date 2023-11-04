package com.prodemy.services;

import com.prodemy.entity.Product;
import com.prodemy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void addToCart(Long id) {

    }

//    public List<Product> filterProductsByName(String productName) {
//        return productRepository.findByProductNameContaining(productName);
//    }

    @Override
    public List<Product> findByKeyword(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    // edit products
    public void saveProductToDB(MultipartFile file, String productName, String productDescription, double productPrice) {
        Product product = new Product();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(fileName.contains("..")) {
            System.out.println("not a a valid file");
        }
        try {
            product.setProductImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setProductName(productName);
        product.setProductDescription(productDescription);
        product.setProductPrice(productPrice);

        productRepository.save(product);

    }
}
