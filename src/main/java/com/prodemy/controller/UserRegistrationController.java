package com.prodemy.controller;

import com.prodemy.entity.User;
import com.prodemy.model.UserDto;
import com.prodemy.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

     public UserRegistrationController(UserService userService) {
     super();
     this.userService = userService;
     }
     @ModelAttribute("user")
     public UserDto userDto() {
         return new UserDto();
     }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping(path = "/users/register")
    public String registerUserAccount(@ModelAttribute("user") UserDto registrationDto) {
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }

}
