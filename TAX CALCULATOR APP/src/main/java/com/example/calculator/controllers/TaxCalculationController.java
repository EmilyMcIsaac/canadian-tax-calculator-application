package com.example.calculator.controllers;

import com.example.calculator.exception.ResourceNotFoundException;
import com.example.calculator.models.*;
import com.example.calculator.services.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tax-calculations")
public class TaxCalculationController {

    private final TaxCalculationService taxCalculationService;
    private final UserService userService;  // Inject the UserService here

    @Autowired
    public TaxCalculationController(TaxCalculationService taxCalculationService, UserService userService) {
        this.taxCalculationService = taxCalculationService;
        this.userService = userService;  // Initialize it via the constructor
    }

    @PostMapping("/calculate")
    public ResponseEntity<TaxCalculation> calculateTax(
            @RequestParam("income") @Positive(message = "Income must be positive") BigDecimal income,
            @RequestParam("province") @NotEmpty String province,
            @RequestParam("userId") Long userId) {
        // Fetch the user from the UserService or UserRepository (not shown here)
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
