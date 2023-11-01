package com.prodemy.web;

import com.prodemy.services.UserService;
import com.prodemy.web.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@NoArgsConstructor
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;

//    public UserRegistrationController(UserService userService) {
//        super();
//        this.userService = userService;
//    }

    public String registerUserAccount(@ModelAttribute("user")UserRegistrationDto registrationDto) {
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }

}
