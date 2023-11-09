package com.prodemy.services;

import com.prodemy.entity.Cart;
import com.prodemy.entity.HistoryPemesanan;
import com.prodemy.entity.Product;
import com.prodemy.entity.User;
import com.prodemy.repository.CartRepository;
import com.prodemy.repository.HistoryPemesananRepository;
import com.prodemy.repository.ProductRepository;
import com.prodemy.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

import java.util.Optional;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private HistoryPemesananRepository historyPemesanantRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void addToCart(long id, String email) {
        Product product = productRepository.findById(id).orElse(null);
        User user = userRepository.findByEmail(email);

        Cart cart = new Cart();
        cart.setProducts(product);
        cart.setUsers(user);
        cart.setStatus("belum terbayar");
        cartRepository.save(cart);
    }

    @Override
    public List<Product> findByKeyword(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    @Override
    public List<Product> findProductByPriceRange(long minPrice, long maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Product> findProductsByPriceRangeAndName(long minPrice, long maxPrice, String name) {
        return productRepository.findProductsByPriceRangeAndName(minPrice, maxPrice, name);
    }

    @Override
    public Product getProductById(long id) {
        Optional<Product> optional = productRepository.findById(id);
        Product product = null;
        if (optional.isPresent()) {
            product = optional.get();
        } else {
            throw new RuntimeException("ID Product tidak dapat ditemukan :: " + id);
        }
        return product;
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void removeProductById(long id) {
        List<Cart> carts = cartRepository.getCartByProductId(id);
        System.out.println("size : " + carts.size());

        if (carts.size() > 0) {
            for (int i = 0; i < carts.size(); i++) {
                carts.get(i).setProducts(null);
                carts.get(i).setUsers(null);
            }
        }
        productRepository.deleteById(id);
    }

    @Override
    public void editProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductInCartByUserId(long id) {
        List<Cart> cart = cartRepository.getCartByUserId(id);
        List<Product> product = new ArrayList<Product>(cart.size());
        if (cart.size() > 0) {
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).getStatus().equals("belum terbayar")) {
                    product.add(productRepository.findById(cart.get(i).getProducts().getId()).orElse(null));
                }
            }
        }

        return product;
    }

    @Override
    public long countPriceProducts(List<Product> products) {
        long countPrice = 0;
        for (int i = 0; i < products.size(); i++) {
            countPrice += products.get(i).getProductPrice();
        }
        return countPrice;
    }

    @Override
    public void addPaymentAndaddDelivery(String paymentMethod, String deliveryMethod, long id) throws Exception {
        List<Cart> cart = cartRepository.getCartByUserId(id);
        LocalDate today = LocalDate.now();
        User user = userRepository.findById(id).orElse(null);
        List<HistoryPemesanan> history = new ArrayList<HistoryPemesanan>(cart.size());
        List<Product> products = new ArrayList<Product>(cart.size());

        if (cart.size() > 0) {
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).getStatus().equals("belum terbayar")) {
                    products.add(productRepository.findById(cart.get(i).getProducts().getId()).orElse(null));
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ini belum melakukan add to cart");
        }

        if (products.size() > 0 && cart.size() > 0) {
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).getStatus().equals("belum terbayar")) {
                    cart.get(i).setStatus("terbayar");
                    cart.get(i).setDeliveryMethod(deliveryMethod);
                    cart.get(i).setPaymentMethod(paymentMethod);
                    cartRepository.save(cart.get(i));
                }
            }
            for (int j = 0; j < products.size(); j++) {
                history.add(new HistoryPemesanan(0, products.get(j).getProductName(), products.get(j).getProductImage(),
                        products.get(j).getProductPrice(), today, user));
                historyPemesanantRepository.save(history.get(j));
            }
        } else {
            throw new Exception("User ini tidak memiliki cart");
        }
    }

    @Override
    public List<HistoryPemesanan> getHistoryByIdUser(long id) {
        List<HistoryPemesanan> historyPemesanan = historyPemesanantRepository.getHistoryByUserId(id);
        return historyPemesanan;
    }
}
