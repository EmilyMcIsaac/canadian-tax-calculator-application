package com.example.calculator;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaxCalculatorApp {
    public static void main(String[] args) {
        SpringApplication.run(TaxCalculatorApp.class, args);
    }
}

