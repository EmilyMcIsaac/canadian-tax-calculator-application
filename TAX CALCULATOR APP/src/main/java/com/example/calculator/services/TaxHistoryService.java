package com.example.calculator.services;

import com.example.calculator.models.*;
import com.example.calculator.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaxHistoryService {

    private final TaxHistoryRepository taxHistoryRepository;

    @Autowired
    public TaxHistoryService(TaxHistoryRepository taxHistoryRepository) {
        this.taxHistoryRepository = taxHistoryRepository;
    }

    // Save a tax calculation to history
    public TaxHistory saveToHistory(TaxHistory taxHistory) {
        return taxHistoryRepository.save(taxHistory);
    }

    // Find all tax history entries by user
    public List<TaxHistory> getHistoryByUser(User user) {
        return taxHistoryRepository.findByUser(user);
    }

    // Find all tax history entries for a specific tax calculation
    public List<TaxHistory> getHistoryByCalculation(TaxCalculation taxCalculation) {
        return taxHistoryRepository.findByTaxCalculation(taxCalculation);
    }
}

