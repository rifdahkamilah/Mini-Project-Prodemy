package com.prodemy.controller;

import com.prodemy.entity.Cart;
import com.prodemy.entity.HistoryPemesanan;
//import com.prodemy.entity.PaymentMethod;
import com.prodemy.entity.Product;
import com.prodemy.global.GlobalData;
import com.prodemy.model.ProductDto;
import com.prodemy.model.ProductEditRequest;
import com.prodemy.model.UserDto;
import com.prodemy.repository.CartRepository;
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
import org.thymeleaf.util.StringUtils;

@Controller
public class ProductController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

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
        model.addAttribute("cartCount", cartCount);
        return "edit_product";
    }

    // @PostMapping("/product/update")
    // public String updateProductPost(@ModelAttribute("products")
    // ProductEditRequest productReq,
    // @RequestParam("productImage") MultipartFile file, @RequestParam("imgName")
    // String nameImage)
    // throws IOException {
    // Product product = new Product();
    // product.setId(productReq.getId());
    // product.setProductName(productReq.getProductName());
    // product.setProductPrice(productReq.getProductPrice());
    // product.setProductDescription(productReq.getProductDescription());
    // String imageUUID;
    // if (!file.isEmpty()) {
    // imageUUID = file.getOriginalFilename();
    // Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, imageUUID);
    // Files.write(fileNameAndPath, file.getBytes());
    // } else {
    // imageUUID = nameImage;
    // }
    // product.setProductImage(imageUUID);
    // productService.addProduct(product);
    // return "redirect:/products";
    // }

    // view detail product

    // @PostMapping("/admin/products/add")
    // public String handleFormSubmit(@ModelAttribute("productDTO") Product product,
    // @RequestParam("mainImage") MultipartFile multipartFile1,
    // @RequestParam("extraImage1") MultipartFile multipartFile2,
    // @RequestParam("extraImage2") MultipartFile multipartFile3,
    // @RequestParam("extraImage3") MultipartFile multipartFile4) throws IOException
    // {

    // String mainImage =
    // StringUtils.cleanPath(multipartFile1.getOriginalFilename());
    // String extraImage1 =
    // StringUtils.cleanPath(multipartFile2.getOriginalFilename());
    // String extraImage2 =
    // StringUtils.cleanPath(multipartFile3.getOriginalFilename());
    // String extraImage3 =
    // StringUtils.cleanPath(multipartFile4.getOriginalFilename());

    // product.setId(product.getId());
    // product.setName(product.getName());
    // //
    // product.setCategory(categoryService.getCategoryById(product.getCategoryId()).get());
    // product.setPrice(product.getPrice());
    // product.setWeight(product.getWeight());
    // product.setDescription(product.getDescription());
    // product.setMainImage(product.getMainImage());
    // product.setExtraImage1(product.getExtraImage1());
    // product.setExtraImage2(product.getExtraImage2());
    // product.setExtraImage3(product.getExtraImage3());
    // // candidate.setProfilePicture(profilePictureFileName);
    // // candidate.setPhotoId(photoIdFileName);
    // // candidate.setDocument(documentFileName);

    // Product savedProduct = productService.addProduct(product);
    // String uploadDir = "product/" + savedProduct.getId();

    // FileUploadUtil.saveFile(uploadDir, mainImage, multipartFile1);
    // FileUploadUtil.saveFile(uploadDir, extraImage1, multipartFile2);
    // FileUploadUtil.saveFile(uploadDir, extraImage2, multipartFile3);
    // FileUploadUtil.saveFile(uploadDir, extraImage3, multipartFile3);

    // return "redirect:/admin/products";
    // }

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

    @GetMapping("/cart/removeItem/{id}")
    public String cartItemRemove(@PathVariable int id) {
        GlobalData.cart.remove(id);
        return "redirect:/cart";
    }

    // @GetMapping("/cart/addPaymentMethod/{method}")
    // public String addPayment(@PathVariable("method") String paymentMethod, long
    // id) {
    // productService.addPayment(paymentMethod, id);
    // return "redirect:/history";
    // }

    // @GetMapping("/cart/addDeliveryMethod/{method}")
    // public String addDelivery(@PathVariable("method") String deliveryMethod, long
    // id) {
    // productService.addDelivery(deliveryMethod, id);
    // return "redirect:/history";
    // }

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
    public String getHisTory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<HistoryPemesanan> historyPemesananan = productService.getHistoryByIdUser(currentUser.getId());

        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("histories", historyPemesananan);
        return "history";
    }

    // @GetMapping("/checkout")
    // public String checkout(Model model) {
    // model.addAttribute("total",
    // GlobalData.cart.stream().mapToDouble(Product::getProductPrice).sum());
    // return "checkout";
    //
    // }

    // payment methods
    // @GetMapping("/cart")
    // public String viewCart(Model model) {
    // List<Product> paymentMethods = productService.getAllPaymentMethods();
    // model.addAttribute("paymentMethods", paymentMethods);
    ////
    //// List<Cart> carts = productService.getCartItems();
    //// model.addAttribute("cartItems", cartItems);
    //
    // return "cart";

}
