package com.banking.banking.controller;

import com.banking.banking.model.User;
import com.banking.banking.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute("user") User user,
            Model model) {

        try {

            userService.registerCustomer(user);

            return "redirect:/login?registered";

        } catch (RuntimeException e) {

            model.addAttribute("error", e.getMessage());

            return "register";
        }
    }
}