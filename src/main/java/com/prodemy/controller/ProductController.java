package com.prodemy.controller;

import com.prodemy.entity.HistoryPemesanan;
import com.prodemy.entity.Product;
import com.prodemy.model.ProductDto;
import com.prodemy.model.ProductEditRequest;
import com.prodemy.model.UserDto;
import com.prodemy.services.ProductService;
import com.prodemy.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/demo/src/main/resources/static/uploads";

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    private String keyword;
    private static int cartCount;

    @GetMapping("/products")
    public String listProducts(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<Product> productInCart = productService.getProductInCartByUserId(currentUser.getId());
        cartCount = productInCart.size();
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
        model.addAttribute("cartCount", cartCount);
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
        model.addAttribute("cartCount", cartCount);

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
        model.addAttribute("cartCount", cartCount);
        return "new_product";
    }

    @PostMapping("/products/add")
    public String productAddPost(@ModelAttribute("products") ProductDto productDTO,
            @RequestParam("productImage") MultipartFile file, @RequestParam("extraImage1") MultipartFile file1,
            @RequestParam("extraImage2") MultipartFile file2, @RequestParam("extraImage3") MultipartFile file3,
            @RequestParam("extraImage4") MultipartFile file4, @RequestParam("imageNameMain") String nameImage,
            @RequestParam("imageNameExtra1") String nameImage1, @RequestParam("imageNameExtra2") String nameImage2,
            @RequestParam("imageNameExtra3") String nameImage3, @RequestParam("imageNameExtra4") String nameImage4)
            throws IOException {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setProductPrice(productDTO.getProductPrice());
        product.setProductDescription(productDTO.getProductDescription());
        String imageUUID;
        String imageExtra1;
        String imageExtra2;
        String imageExtra3;
        String imageExtra4;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());

            imageExtra1 = file1.getOriginalFilename();
            Path fileNameAndPathExtra1 = Paths.get(UPLOAD_DIRECTORY, imageExtra1);
            Files.write(fileNameAndPathExtra1, file1.getBytes());

            imageExtra2 = file2.getOriginalFilename();
            Path fileNameAndPathExtra2 = Paths.get(UPLOAD_DIRECTORY, imageExtra2);
            Files.write(fileNameAndPathExtra2, file2.getBytes());

            imageExtra3 = file3.getOriginalFilename();
            Path fileNameAndPathExtra3 = Paths.get(UPLOAD_DIRECTORY, imageExtra3);
            Files.write(fileNameAndPathExtra3, file3.getBytes());

            imageExtra4 = file4.getOriginalFilename();
            Path fileNameAndPathExtra4 = Paths.get(UPLOAD_DIRECTORY, imageExtra4);
            Files.write(fileNameAndPathExtra4, file4.getBytes());
        } else {
            imageUUID = nameImage;
            imageExtra1 = nameImage1;
            imageExtra2 = nameImage2;
            imageExtra3 = nameImage3;
            imageExtra4 = nameImage4;
        }
        product.setProductImage(imageUUID);
        product.setExtraImage1(imageExtra1);
        product.setExtraImage2(imageExtra2);
        product.setExtraImage3(imageExtra3);
        product.setExtraImage4(imageExtra4);
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
        model.addAttribute("cartCount", cartCount);
        return "edit_product";
    }

    @PostMapping("/product/update")
    public String updateProductPost(@ModelAttribute("products") ProductEditRequest productReq,
            @RequestParam("productImage") MultipartFile file, @RequestParam("extraImage1") MultipartFile file1,
            @RequestParam("extraImage2") MultipartFile file2, @RequestParam("extraImage3") MultipartFile file3,
            @RequestParam("extraImage4") MultipartFile file4, @RequestParam("imageNameMain") String nameImage,
            @RequestParam("imageNameExtra1") String nameImage1, @RequestParam("imageNameExtra2") String nameImage2,
            @RequestParam("imageNameExtra3") String nameImage3, @RequestParam("imageNameExtra4") String nameImage4)
            throws IOException {
        Product product = new Product();
        product.setId(productReq.getId());
        product.setProductName(productReq.getProductName());
        product.setProductPrice(productReq.getProductPrice());
        product.setProductDescription(productReq.getProductDescription());
        System.out.println("file main : " + file.getOriginalFilename());
        System.out.println("file main 1 : " + file1.getOriginalFilename());
        System.out.println("file main 2: " + file2.getOriginalFilename());
        System.out.println("file main 3: " + file3.getOriginalFilename());
        System.out.println("file main 4: " + file4.getOriginalFilename());
        String imageUUID;
        String imageExtra1;
        String imageExtra2;
        String imageExtra3;
        String imageExtra4;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());

        } else {
            imageUUID = nameImage;
        }

        if (!file1.isEmpty()) {
            imageExtra1 = file1.getOriginalFilename();
            Path fileNameAndPathExtra1 = Paths.get(UPLOAD_DIRECTORY, imageExtra1);
            Files.write(fileNameAndPathExtra1, file1.getBytes());
        } else {
            imageExtra1 = nameImage1;
        }

        if (!file2.isEmpty()) {
            imageExtra2 = file2.getOriginalFilename();
            Path fileNameAndPathExtra2 = Paths.get(UPLOAD_DIRECTORY, imageExtra2);
            Files.write(fileNameAndPathExtra2, file2.getBytes());
        } else {
            imageExtra2 = nameImage2;
        }

        if (!file3.isEmpty()) {
            imageExtra3 = file3.getOriginalFilename();
            Path fileNameAndPathExtra3 = Paths.get(UPLOAD_DIRECTORY, imageExtra3);
            Files.write(fileNameAndPathExtra3, file3.getBytes());
        } else {
            imageExtra3 = nameImage3;
        }

        if (!file4.isEmpty()) {
            imageExtra4 = file4.getOriginalFilename();
            Path fileNameAndPathExtra4 = Paths.get(UPLOAD_DIRECTORY, imageExtra4);
            Files.write(fileNameAndPathExtra4, file4.getBytes());
        } else {
            imageExtra4 = nameImage4;
        }
        product.setProductImage(imageUUID);
        product.setExtraImage1(imageExtra1);
        product.setExtraImage2(imageExtra2);
        product.setExtraImage3(imageExtra3);
        product.setExtraImage4(imageExtra4);
        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping({ "/product/viewproduct/{id}" })
    public String viewProduct(Model model, @PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("cartCount", cartCount);
        return "view_product";
    }

    // add to cart
    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") int id, Authentication userReq) {
        productService.addToCart(id, userReq.getName());
        return "redirect:/products?successAddToCart";
    }

    @GetMapping("/cart")
    public String cartGet(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<Product> productInCart = productService.getProductInCartByUserId(currentUser.getId());
        System.out.println(productInCart.size());
        long countPrice = productService.countPriceProducts(productInCart);

        model.addAttribute("total", countPrice);
        model.addAttribute("products", productInCart);
        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("cartCount", productInCart.size());
        return "cart";
    }

    @GetMapping("/users/cart/current")
    public String getCartByUserId(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<Product> productInCart = productService.getProductInCartByUserId(currentUser.getId());

        model.addAttribute("products", productInCart);
        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("cartCount", cartCount);
        return "cartDetail";
    }

    @PostMapping("/cart/checkout")
    public String checkout(@RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("deliveryMethod") String deliveryMethod) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        productService.addPaymentAndaddDelivery(paymentMethod, deliveryMethod, currentUser.getId());
        return "redirect:/products?successAddToCart";
    }

    @GetMapping("/histori")
    public String getHisToryCurrentUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<HistoryPemesanan> historyPemesananan = productService.getHistoryByIdUser(currentUser.getId());

        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("histories", historyPemesananan);
        return "history";
    }

    @GetMapping("/users/cart/history/{id}")
    public String getHistoryUser(Model model, @PathVariable("id") long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<HistoryPemesanan> historyPemesananan = productService.getHistoryByIdUser(id);

        model.addAttribute("id", id);
        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("histories", historyPemesananan);
        return "viewHistoryUser";
    }

    @GetMapping("/users/cart/{id}")
    public String getCartUser(Model model, @PathVariable("id") long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<Product> productInCart = productService.getProductInCartByUserId(id);

        model.addAttribute("id", id);
        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("products", productInCart);
        return "viewCartUser";
    }

}
