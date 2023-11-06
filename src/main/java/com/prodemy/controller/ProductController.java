package com.prodemy.controller;

import com.prodemy.entity.Product;
import com.prodemy.model.ProductDto;
import com.prodemy.services.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
// @RequestMapping("/products")
public class ProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

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
    @GetMapping("/products/getByName")
    public String getProductsByName(Model model, @RequestParam String keyword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", "USER");
        }
        this.keyword = keyword;
        if (keyword != null) {
            model.addAttribute("products", productService.findByKeyword(keyword));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products"; // html file
    }

    @GetMapping("/products/getByPriceRange")
    public String getProductsByPriceRange(Model model, @RequestParam("minPrice") Long minPrice,
            @RequestParam("maxPrice") Long maxPrice) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", "USER");
        }
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

    // pagination
    // @GetMapping("/")
    // public String viewHomePage(Model model) {
    // return findPaginated(1, model);
    // }
    //
    // @GetMapping("/page/{pageNo}")
    // public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
    // Model model) {
    // int pageSize = 4;
    //
    // Page<Product> page = productService.findPaginated(pageNo, pageSize);
    // List<Product> listProduct = page.getContent();
    //
    // model.addAttribute("currentPage", pageNo);
    // model.addAttribute("totalPages", page.getTotalPages());
    // model.addAttribute("totalItems", page.getTotalElements());
    // model.addAttribute("listProduct", listProduct);
    // return "products";
    // }

    // Product Section
    @GetMapping("/showNewProductForm")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("products", product);
        return "new_product";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("products") ProductDto product,
            @RequestParam("productImage") MultipartFile file)
            throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY,
                file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        System.out.println(file.getOriginalFilename());
        // productService.saveProduct(product);
        return "redirect:/showNewProductForm?success";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(value = "id") long id) {
        this.productService.deleteProductById(id);
        return "redirect:/products";
    }
}
