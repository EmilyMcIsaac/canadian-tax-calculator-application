package com.example.calculator.controllers;

import com.example.calculator.dto.TaxCalculationRequest;
import com.example.calculator.exception.ResourceNotFoundException;
import com.example.calculator.models.*;
import com.example.calculator.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tax-calculations")
public class TaxCalculationController {

    private final TaxCalculationService taxCalculationService;
    private final UserService userService;

    @Autowired
    public TaxCalculationController(TaxCalculationService taxCalculationService, UserService userService) {
        this.taxCalculationService = taxCalculationService;
        this.userService = userService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<TaxCalculation> calculateTax(@RequestBody TaxCalculationRequest request) {
        BigDecimal income = request.getIncome();
        String province = request.getProvince().toLowerCase();
        Long userId = request.getUserId();
        int taxYear = request.getTaxYear(); // Add the taxYear

        // Fetch the user from the UserService
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Calculate the tax with the tax year
        TaxCalculation calculation = taxCalculationService.createTaxCalculation(income, province, taxYear, user);

        return ResponseEntity.ok(calculation);
    }

    // Endpoint to soft delete a tax calculation
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTaxCalculation(@PathVariable Long id) {
        taxCalculationService.softDeleteTaxCalculation(id);
        return ResponseEntity.ok(Map.of("message", "Tax calculation deleted successfully"));
    }

    // Get all tax calculations for the current user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaxCalculation>> getAllTaxCalculationsForUser(@PathVariable Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<TaxCalculation> calculations = taxCalculationService.getAllForUser(user);
        return ResponseEntity.ok(calculations);
    }
}

