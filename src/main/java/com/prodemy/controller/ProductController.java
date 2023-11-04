package com.prodemy.controller;

import com.prodemy.entity.Product;
import com.prodemy.repository.ProductRepository;
import com.prodemy.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    //filter by name
//    @GetMapping("/products")
//    public String listProducts(@RequestParam(name = "productName", required = false) String productName, Model model) {
//        if (productName != null) {
//            List<Product> filteredProducts = productService.filterProductsByName(productName);
//            model.addAttribute("products", filteredProducts);
//        } else {
//            List<Product> allProducts = productService.getAllProducts();
//            model.addAttribute("products", allProducts);
//        }
//        return "products";
//    }

    @GetMapping("/products")
    public String getProducts(Model model, String keyword) {
        if(keyword != null) {
            model.addAttribute("products", productService.findByKeyword(keyword));
        }
        else {
            model.addAttribute("products", productService.getAllProducts());
        }
        System.out.print("keyword " + keyword);
        return "products"; //html file
    }

}
