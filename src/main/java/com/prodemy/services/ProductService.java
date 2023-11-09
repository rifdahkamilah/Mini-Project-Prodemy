package com.prodemy.services;

import com.prodemy.entity.HistoryPemesanan;
import com.prodemy.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(long id);

    void addToCart(long id, String email);

    List<Product> findByKeyword(String keyword);

    List<Product> findProductByPriceRange(long minPrice, long maxPrice);

    List<Product> findProductsByPriceRangeAndName(long minPrice, long maxPrice, String name);

    void addProduct(Product product);

    public void editProduct(Product product);

    void removeProductById(long id);

    public void addPaymentAndaddDelivery(String paymentMethod, String addDelivery, long id) throws Exception;

    public List<Product> getProductInCartByUserId(long id);

    public long countPriceProducts(List<Product> products);

    public List<HistoryPemesanan> getHistoryByIdUser(long id);


}
