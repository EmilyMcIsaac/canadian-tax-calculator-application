package com.example.calculator.controllers;

import com.example.calculator.models.*;
import com.example.calculator.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tax-brackets")
public class TaxBracketController {

    private final TaxBracketService taxBracketService;

    @Autowired
    public TaxBracketController(TaxBracketService taxBracketService) {
        this.taxBracketService = taxBracketService;
    }

    // Get tax brackets by region and year
    @GetMapping("/{region}/{year}")
    public ResponseEntity<List<TaxBracket>> getTaxBrackets(@PathVariable String region, @PathVariable Integer year) {
        return ResponseEntity.ok(taxBracketService.getTaxBracketsByRegionAndYear(region, year));
    }
}
