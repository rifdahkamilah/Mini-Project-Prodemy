package com.prodemy.controller;


import com.prodemy.model.UserDto;
import com.prodemy.services.UserService;
import com.prodemy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/admin")
    public String adminHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto currentUser = userService.getCurrentUser(auth.getName());
        model.addAttribute("nameCurrentUser", currentUser.getName());
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", "USER");
        }
        return "adminHome";
    }

    @GetMapping("/users")
    public String getUser(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/add")
    public String getUserAdd(Model model) {
        model.addAttribute("user", new User());
        return "new_user";
    }

    @PostMapping("/users/add")
    public String postUserAdd(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/update/{id}")
    public String updateUser(@PathVariable(value = "id") long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "new_user";
    }

    @GetMapping({"/users/viewuser/{id}"})
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
//
//
//        model.addAttribute("user", new User());
//        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("nameCurrentUser", currentUser.getName());
        return "view_user";
    }

//    @GetMapping("/admin/users/update/{id}")
//    public String updateUser(@PathVariable int id, Model model) {
//        com.prodemy.entity.User user = userService.getUserById(id);
//        if(user.isPresent()) {
//            model.addAttribute("user", user.get());
//            return "usersAdd";
//        } else
//            return "404";
//    }
}
