package com.example.calculator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "redirect:/homepage.html";  // Redirect to static file
    }

    // Login page
    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";  // Redirect to static file
    }

    // Register page
    @GetMapping("/register")
    public String register() {
        return "redirect:/register.html";  // Redirect to static file
    }

}
