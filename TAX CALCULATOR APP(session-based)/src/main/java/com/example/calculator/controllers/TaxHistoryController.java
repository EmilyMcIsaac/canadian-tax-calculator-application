package com.example.calculator.controllers;

import com.example.calculator.models.*;
import com.example.calculator.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tax-history")
public class TaxHistoryController {

    private final TaxHistoryService taxHistoryService;

    @Autowired
    public TaxHistoryController(TaxHistoryService taxHistoryService) {
        this.taxHistoryService = taxHistoryService;
    }

    // Save tax calculation to history
    @PostMapping("/")
    public ResponseEntity<TaxHistory> saveToHistory(@RequestBody TaxHistory taxHistory) {
        return ResponseEntity.ok(taxHistoryService.saveToHistory(taxHistory));
    }

    // Get all tax history entries for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaxHistory>> getTaxHistoryForUser(@PathVariable Long userId) {
        User user = new User(); // Replace with actual user retrieval logic
        user.setId(userId);
        return ResponseEntity.ok(taxHistoryService.getHistoryByUser(user));
    }

    // Get all tax history entries for a tax calculation
    @GetMapping("/calculation/{calculationId}")
    public ResponseEntity<List<TaxHistory>> getTaxHistoryForCalculation(@PathVariable Long calculationId) {
        TaxCalculation calculation = new TaxCalculation(); // Replace with actual calculation retrieval
        calculation.setId(calculationId);
        return ResponseEntity.ok(taxHistoryService.getHistoryByCalculation(calculation));
    }
}

