package com.prodemy.controller;

import com.prodemy.entity.Product;
import com.prodemy.repository.ProductRepository;
import com.prodemy.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
// @RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    private String keyword;

    @GetMapping("/products")
    public String listProducts(Model model) {
        // List<Product> products = productRepository.findAll();
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // if (auth.getAuthorities().stream().anyMatch(a ->
        // a.getAuthority().equals("ADMIN"))) {
        // model.addAttribute("role", "ADMIN");
        // } else {
        // model.addAttribute("role", "USER");
        // }
        // model.addAttribute("products", products);
        return this.getProductsByName(model, null);
    }

    // filter by name
    // @GetMapping("/products")
    // public String listProducts(@RequestParam(name = "productName", required =
    // false) String productName, Model model) {
    // if (productName != null) {
    // List<Product> filteredProducts =
    // productService.filterProductsByName(productName);
    // model.addAttribute("products", filteredProducts);
    // } else {
    // List<Product> allProducts = productService.getAllProducts();
    // model.addAttribute("products", allProducts);
    // }
    // return "products";
    // }

    @GetMapping("/products/getByName")
    public String getProductsByName(Model model, @RequestParam String keyword) {
        this.keyword = keyword;
        if (keyword != null) {
            model.addAttribute("products", productService.findByKeyword(keyword));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products"; // html file
    }

    @GetMapping("/products/getByPriceRange")
    public String getProductsByPriceRange(Model model, @RequestParam("minPrice") double minPrice,
            @RequestParam("maxPrice") double maxPrice) {
        System.out.println(this.keyword);
        if (this.keyword != "" && minPrice >= 0 && maxPrice > 0) {
            model.addAttribute("products",
                    productService.findProductsByPriceRangeAndName(minPrice, maxPrice, this.keyword));
        } else if (minPrice >= 0 && maxPrice > 0) {
            model.addAttribute("products", productService.findProductByPriceRange(minPrice, maxPrice));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products"; // html file
    }

}
