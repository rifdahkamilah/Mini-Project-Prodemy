package com.prodemy.services;

import com.prodemy.entity.Cart;
import com.prodemy.entity.Product;
import com.prodemy.entity.User;
import com.prodemy.repository.CartRepository;
import com.prodemy.repository.ProductRepository;
import com.prodemy.repository.UserRepository;

import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImplementation implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    // @Autowired
    // private ProductService productService;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // @Override
    // public Product getProductById(long id) {
    // return productRepository.findById(id).orElse(null);
    // }

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
    // edit products
    // @Override
    // public List<Product> saveProductToDB(MultipartFile file, String productName,
    // String productDescription, double productPrice) {
    // Product product = new Product();
    // String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    // if(fileName.contains("..")) {
    // System.out.println("not a a valid file");
    // }
    // try {
    // product.setProductImage(Base64.getEncoder().encodeToString(file.getBytes()));
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // product.setProductName(productName);
    // product.setProductDescription(productDescription);
    // product.setProductPrice(productPrice);
    //
    // productRepository.save(product);

    @Override
    public List<Product> findProductByPriceRange(long minPrice, long maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Product> findProductsByPriceRangeAndName(long minPrice, long maxPrice, String name) {
        return productRepository.findProductsByPriceRangeAndName(minPrice, maxPrice, name);
    }

    // @Override
    // public void saveProduct(Product product) {
    // this.productRepository.save(product);
    // }

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

    // @Override
    // public void deleteProductById(long id) {
    // this.productRepository.deleteById(id);
    // }

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
            }
        }
        productRepository.deleteById(id);
    }

    @Override
    public void editProduct(Product product) {
        productRepository.save(product);
    }
    // @Override
    // public List<Product> getAllPaymentMethods() {
    // return productRepository.findAll();
    // }

    @Override
    public void addPayment(String methodPayment, long id) {
        Cart cart = cartRepository.findById(id).orElse(null);
        cart.setMetodePembayaran(methodPayment);

        cartRepository.save(cart);
    }

    @Override
    public List<Product> getProductInCartByUserId(long id) {
        List<Cart> cart = cartRepository.getCartByUserId(id);
        List<Product> product = new ArrayList<Product>(cart.size());
        if (cart.size() > 0) {
            for (int i = 0; i < cart.size(); i++) {
                product.add(productRepository.findById(cart.get(i).getProducts().getId()).orElse(null));
            }
        }
        return product;
    }

}
