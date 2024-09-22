package com.example.calculator.services;

import com.example.calculator.exception.ResourceNotFoundException;
import com.example.calculator.models.*;
import com.example.calculator.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaxHistoryService {

    @Autowired
    private TaxHistoryRepository taxHistoryRepository;

    @Autowired
    private TaxCalculationRepository taxCalculationRepository;

    // Save a tax calculation to history
    public TaxHistory saveToHistory(Long calculationId) {
        TaxCalculation taxCalculation = taxCalculationRepository.findByIdAndDeletedFalse(calculationId)
                .orElseThrow(() -> new ResourceNotFoundException("Calculation not found"));

        // Create and save a new TaxHistory entry
        TaxHistory taxHistory = new TaxHistory();
        taxHistory.setTaxCalculation(taxCalculation);
        taxHistory.setSavedAt(LocalDateTime.now());  // Set the saved timestamp
        // Set saved = true in the tax calculation
        taxCalculation.setSaved(true);
        taxCalculationRepository.save(taxCalculation);

        return taxHistoryRepository.save(taxHistory);


    }

    // Get all tax history entries for a user
    public List<TaxHistory> getUserTaxHistory(User user) {
        return taxHistoryRepository.findByUserAndNonDeletedCalculations(user);
    }

    // Soft-delete a tax history entry
    public void deleteFromHistory(Long historyId) {
        TaxHistory taxHistory = taxHistoryRepository.findById(historyId)
                .orElseThrow(() -> new ResourceNotFoundException("Tax history not found"));

        // Soft delete the tax calculation associated with this history entry
        TaxCalculation taxCalculation = taxHistory.getTaxCalculation();
        taxCalculation.setDeleted(true);
        taxCalculationRepository.save(taxCalculation);
    }
}
