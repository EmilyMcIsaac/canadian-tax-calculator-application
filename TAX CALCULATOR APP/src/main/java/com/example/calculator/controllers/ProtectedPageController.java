package com.example.calculator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProtectedPageController {

    @GetMapping("/account")
    public String account() {
        return "redirect:/account.html";  // This page is protected
    }

    @GetMapping("/calculate-tax")
    public String calculateTax() {
        return "redirect:/calculate-tax.html";  // This page is protected
    }

    @GetMapping("/tax-history")
    public String taxHistory() {
        return "redirect:/tax-history.html";  // This page is protected
    }
}

