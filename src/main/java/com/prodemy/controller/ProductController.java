package com.prodemy.controller;

import com.prodemy.entity.Products;
import com.prodemy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    public String listProducts(Model model) {
        List<Products> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }
}
