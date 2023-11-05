package com.prodemy.services;

import com.prodemy.entity.Product;
import com.prodemy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImplementation implements ProductService {
    @Autowired
    private ProductRepository productRepository;

//    @Autowired
//    private ProductService productService;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void addToCart(long id) {

    }

    @Override
    public List<Product> findByKeyword(String keyword) {
        return productRepository.findByKeyword(keyword);
    }
    // edit products
//    @Override
//    public List<Product> saveProductToDB(MultipartFile file, String productName, String productDescription, double productPrice) {
//        Product product = new Product();
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        if(fileName.contains("..")) {
//            System.out.println("not a a valid file");
//        }
//        try {
//            product.setProductImage(Base64.getEncoder().encodeToString(file.getBytes()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        product.setProductName(productName);
//        product.setProductDescription(productDescription);
//        product.setProductPrice(productPrice);
//
//        productRepository.save(product);

    @Override
    public List<Product> findProductByPriceRange(long minPrice, long maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Product> findProductsByPriceRangeAndName(long minPrice, long maxPrice, String name) {
        return productRepository.findProductsByPriceRangeAndName(minPrice, maxPrice, name);
    }

    @Override
    public void saveProduct(Product product) {
        this.productRepository.save(product);
    }

//    @Override
//    public Product getProductById(long id) {
//        Optional<Product> optional = productRepository.findById(id);
//        Product product = null;
//        if(optional.isPresent()) {
//            product = optional.get();
//        } else {
//            throw new RuntimeException("ID Product tidak dapat ditemukan :: " + id);
//        }
//        return product;
//    }

    @Override
    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

//    @Override
//    public Page<Product> findPaginated(int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//        return this.productRepository.findAll(pageable);
//    }


}
