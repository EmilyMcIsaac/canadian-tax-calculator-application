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


        // Fetch the user from the UserService
        User user = userService.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Calculate the tax
        TaxCalculation calculation = taxCalculationService.createTaxCalculation(income, province, user);

        return ResponseEntity.ok(calculation);
    }

    // Get all tax calculations for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaxCalculation>> getAllCalculationsForUser(@PathVariable Long userId) {
        User user = new User(); // Replace with actual user retrieval logic
        user.setId(userId);
        return ResponseEntity.ok(taxCalculationService.getAllCalculationsByUser(user));
    }

    // Soft delete a tax calculation
    @DeleteMapping("/{calculationId}")
    public ResponseEntity<Void> deleteTaxCalculation(@PathVariable Long calculationId) {
        taxCalculationService.deleteTaxCalculation(calculationId);
        return ResponseEntity.noContent().build();
    }

    // Restore a deleted calculation
    @PostMapping("/restore/{calculationId}")
    public ResponseEntity<Void> restoreTaxCalculation(@PathVariable Long calculationId) {
        taxCalculationService.restoreDeletedCalculation(calculationId);
        return ResponseEntity.noContent().build();
    }
}
