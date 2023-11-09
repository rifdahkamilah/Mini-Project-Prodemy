package com.prodemy.controller;

import com.prodemy.model.AdminRoleEditRequest;
import com.prodemy.model.UserDto;
import com.prodemy.services.ProductService;
import com.prodemy.services.UserService;
import com.prodemy.entity.Product;
import com.prodemy.entity.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    private static int cartCount;

    @GetMapping("/")
    public String adminHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<Product> productInCart = productService.getProductInCartByUserId(currentUser.getId());
        cartCount = productInCart.size();
        model.addAttribute("nameCurrentUser", currentUser.getName());
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", "USER");
        }
        model.addAttribute("cartCount", cartCount);
        return "adminHome";
    }

    @GetMapping("/users")
    public String getUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        List<User> user = userService.getAllUsers(auth.getName());
        model.addAttribute("users", user);
        model.addAttribute("nameCurrentUser", currentUser.getName());
        model.addAttribute("cartCount", cartCount);
        return "users";
    }

    @GetMapping("/users/add")
    public String getUserAdd(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("cartCount", cartCount);
        return "new_user";
    }

    @PostMapping("/users/add")
    public String postUserAdd(@ModelAttribute("user") UserDto user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.removeUserById(id);
        return "redirect:/users";
    }

    @GetMapping({ "/users/viewuser/{id}" })
    public String viewProduct(Model model, @PathVariable(value = "id") long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        User user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            System.out.println("YAY");
        } else {
            model.addAttribute("user", new User());
            System.out.println("Bag of d**ks");
        }
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("nameCurrentUser", currentUser.getName());
        return "view_user";
    }

    @GetMapping("/users/update/{id}")
    public String updateUserGet(@PathVariable int id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());

        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("nameCurrentUser", currentUser.getName());
        return "update_user";
    }

    @PostMapping("/users/update")
    public String updateUserPost(@ModelAttribute("user") AdminRoleEditRequest user) {
        userService.editUserByAdmin(user);
        return "redirect:/users?UpdateSuccess";
    }
}
