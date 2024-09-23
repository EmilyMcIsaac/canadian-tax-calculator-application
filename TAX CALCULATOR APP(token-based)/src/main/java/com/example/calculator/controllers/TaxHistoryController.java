package com.example.calculator.controllers;

import com.example.calculator.exception.ResourceNotFoundException;
import com.example.calculator.models.*;
import com.example.calculator.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tax-history")
public class TaxHistoryController {

    private final UserService userService;
    private final TaxHistoryService taxHistoryService;

    @Autowired
    public TaxHistoryController(UserService userService, TaxHistoryService taxHistoryService) {
        this.userService = userService;
        this.taxHistoryService = taxHistoryService;
    }

    @PostMapping("/save/{calculationId}")
    public ResponseEntity<TaxHistory> saveToHistory(@PathVariable Long calculationId) {
        TaxHistory savedHistory = taxHistoryService.saveToHistory(calculationId);
        return ResponseEntity.ok(savedHistory);
    }

    // Save a tax calculation to history
    @PostMapping("/save")
    public ResponseEntity<?> saveTaxToHistory(@RequestBody Long calculationId) {
        TaxHistory savedHistory = taxHistoryService.saveToHistory(calculationId);
        return ResponseEntity.ok(savedHistory);
    }

    // Get all tax history entries for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaxHistory>> getTaxHistoryForUser(@PathVariable Long userId) {
        User user = userService.findByIdOrThrow(userId);

        List<TaxHistory> history = taxHistoryService.getUserTaxHistory(user);
        return ResponseEntity.ok(history);
    }

    // Soft delete a tax history entry
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTaxHistoryEntry(@PathVariable Long id) {
        taxHistoryService.deleteFromHistory(id);
        return ResponseEntity.ok(Map.of("message", "Tax history entry deleted successfully"));
    }
}

