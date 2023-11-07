package com.prodemy.controller;

import com.prodemy.entity.Product;
import com.prodemy.global.GlobalData;
import com.prodemy.model.ProductDto;
import com.prodemy.model.ProductEditRequest;
import com.prodemy.model.UserDto;
import com.prodemy.services.ProductService;
import com.prodemy.services.UserService;

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
public class ProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    private String keyword;

    @GetMapping("/products")
    public String listProducts(Model model) {
        return this.getProductsByName(model, null);
    }

    // filter by name
    @GetMapping("/products/getByName")
    public String getProductsByName(Model model, @RequestParam String keyword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        this.keyword = keyword;

        model.addAttribute("nameCurrentUser", currentUser.getName());

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", "USER");
        }

        if (keyword != null) {
            model.addAttribute("products", productService.findByKeyword(keyword));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products"; // html file
    }

    @GetMapping("/products/getByPriceRange")
    public String getProductsByPriceRange(Model model, @RequestParam("minPrice") String minPriceInput,
            @RequestParam("maxPrice") String maxPriceInput) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        long maxPrice = 0;
        long minPrice = 0;

        if (minPriceInput != "" && maxPriceInput != "") {
            maxPrice = (long) Integer.parseInt(maxPriceInput);
            minPrice = (long) Integer.parseInt(minPriceInput);
        }

        model.addAttribute("nameCurrentUser", currentUser.getName());

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", "USER");
        }

        System.out.println(this.keyword);
        if (this.keyword != null && minPrice >= 0 && maxPrice > 0) {
            model.addAttribute("products",
                    productService.findProductsByPriceRangeAndName(minPrice, maxPrice, this.keyword));
        } else if (minPrice >= 0 && maxPrice > 0) {
            model.addAttribute("products", productService.findProductByPriceRange(minPrice, maxPrice));
        } else {
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products"; // html file
    }

    @GetMapping("/products/add")
    public String productAddGet(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("products", new Product());
        return "new_product";
    }

    @PostMapping("/products/add")
    public String productAddPost(@ModelAttribute("products") ProductDto productDTO,
            @RequestParam("productImage") MultipartFile file, @RequestParam("imgName") String nameImage)
            throws IOException {
        Product product = new Product();
        // product.setId();
        product.setProductName(productDTO.getProductName());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductDescription(productDTO.getProductDescription());
        String imageUUID;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = nameImage;
        }
        product.setProductImage(imageUUID);
        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        productService.removeProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/product/update/{id}")
    public String updateProductGet(@PathVariable long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getCurrentUser(auth.getName());
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("nameCurrentUser", user.getName());
        return "edit_product";
    }

    @PostMapping("/product/update")
    public String updateProductPost(@ModelAttribute("products") ProductEditRequest productReq,
            @RequestParam("productImage") MultipartFile file, @RequestParam("imgName") String nameImage)
            throws IOException {
        Product product = new Product();
        product.setId(productReq.getId());
        product.setProductName(productReq.getProductName());
        product.setProductPrice(productReq.getProductPrice());
        product.setProductDescription(productReq.getProductDescription());
        String imageUUID;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = nameImage;
        }
        product.setProductImage(imageUUID);
        productService.addProduct(product);
        return "redirect:/products";
    }

    // view detail product

    @GetMapping({ "/product/viewproduct/{id}" })
    public String viewProduct(Model model, @PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("nameCurrentUser", currentUser.getName());
        return "view_product";
    }

    // add to cart
    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") int id, Authentication userReq) {
        productService.addToCart(id, userReq.getName());
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cartGet(Model model) {
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getProductPrice).sum());
        model.addAttribute("cart", GlobalData.cart);
        return "cart";
    }

    @GetMapping("cart/removeItem/{id}")
    public String cartItemRemove(@PathVariable int id) {
        GlobalData.cart.remove(id);
        return "redirect:/cart";
    }

    // @GetMapping("/checkout")
    // public String checkout(Model model) {
    // model.addAttribute("total",
    // GlobalData.cart.stream().mapToDouble(Product::getProductPrice).sum());
    // return "checkout";
    //
    // }
}
