package com.prodemy.controller;

import com.prodemy.entity.User;
import com.prodemy.model.UserDto;
import com.prodemy.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

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

//    @GetMapping("/edit-profile/{id}")
//    public String showEditProfileForUpdate(@PathVariable( value = "id") long id, Model model) {
//
////        // get mahasiswa from the service
////        User user = userService.getUserById(id);
//        User user = userService.getUserById(id);
//
//        //set mahasiswa as a model attribute to pre-populate the form
//        model.addAttribute("user", user);
//        return "edit_profile"; // buat menampilkan
//    }
//
//    @PostMapping("/edit-profile")
//    public String updateProfile(@ModelAttribute("user") User user) {
//        // save mahasiswa to database
//        userService.saveUser(user);//harusnya nilainya null karena belum diinstansiasi, tidak perlu diinstansiasi karena sudah diinstansiasi oleh spring
//        return "redirect:/";	// buat menambahkan mahasiswa
//    }



}
