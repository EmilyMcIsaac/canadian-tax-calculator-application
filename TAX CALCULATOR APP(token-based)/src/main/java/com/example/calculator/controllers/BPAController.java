package com.example.calculator.controllers;

import com.example.calculator.services.BPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/bpa")
public class BPAController {

    @Autowired
    private BPAService bpaService;

    // API to get the BPA for a given region and year
    @GetMapping("/get")
    public ResponseEntity<BigDecimal> getBpa(
            @RequestParam String region,
            @RequestParam int year,
            @RequestParam BigDecimal income) {

        BigDecimal bpa = bpaService.getBpa(region, year, income);
        return ResponseEntity.ok(bpa);
    }
}
