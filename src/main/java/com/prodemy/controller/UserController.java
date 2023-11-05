package com.prodemy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.prodemy.model.RequestEditUser;
import com.prodemy.model.UserDto;
import com.prodemy.services.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/view/current")
    public String viewUser(Authentication userReq, Model model) {
        UserDto user = userService.getCurrentUser(userReq.getName());
        model.addAttribute("user", user);
        return "viewprofile";
    }

    @GetMapping("/users/edit/current")
    public String editUser(Authentication userReq, Model model) {
        UserDto user = userService.getCurrentUser(userReq.getName());
        model.addAttribute("user", user);
        return "editprofile";
    }

    @PostMapping("/users/edit/current")
    public String editUserAccount(Authentication emailReq, @ModelAttribute("user") RequestEditUser requestModel) {
        userService.editUser(emailReq.getName(), requestModel);
        return "redirect:/users/view/current?success";
    }
}
