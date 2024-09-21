package com.example.calculator.services;

import com.example.calculator.models.*;
import com.example.calculator.repositories.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaxBracketService {

    private final TaxBracketRepository taxBracketRepository;

    @Autowired
    public TaxBracketService(TaxBracketRepository taxBracketRepository) {
        this.taxBracketRepository = taxBracketRepository;
    }

    // Get tax brackets by region and year
    public List<TaxBracket> getTaxBracketsByRegionAndYear(String region, Integer year) {
        return taxBracketRepository.findByRegionAndYear(region, year);
    }
}
