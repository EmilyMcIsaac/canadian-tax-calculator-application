package com.example.calculator.services;

import com.example.calculator.exception.ResourceNotFoundException;
import com.example.calculator.models.*;
import com.example.calculator.repositories.*;
import com.example.calculator.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaxCalculationService {

    private final TaxCalculationRepository taxCalculationRepository;
    private final TaxBracketRepository taxBracketRepository;

    @Autowired
    public TaxCalculationService(TaxCalculationRepository taxCalculationRepository, TaxBracketRepository taxBracketRepository) {
        this.taxCalculationRepository = taxCalculationRepository;
        this.taxBracketRepository = taxBracketRepository;
    }

    // Create a new tax calculation
    public TaxCalculation createTaxCalculation(BigDecimal income, String province, User user) {

        // Fetch federal tax brackets
        List<TaxBracket> federalBrackets = taxBracketRepository.findByRegionAndYear("federal", 2024);

        // Fetch provincial tax brackets for the selected province
        List<TaxBracket> provincialBrackets = taxBracketRepository.findByRegionAndYear(province.toLowerCase(), 2024);

        System.out.println("Fetched Provincial Brackets: " + provincialBrackets);

        // Calculate federal and provincial taxes
        BigDecimal federalTax = calculateTaxForBrackets(income, federalBrackets);
        BigDecimal provincialTax = calculateTaxForBrackets(income, provincialBrackets);

        // Calculate net income after taxes
        BigDecimal netIncome = income.subtract(federalTax).subtract(provincialTax);

        // Create a new TaxCalculation object with calculated results
        TaxCalculation taxCalculation = new TaxCalculation();
        taxCalculation.setIncome(income);
        taxCalculation.setRegion(province);
        taxCalculation.setNetFederalTax(federalTax);
        taxCalculation.setNetProvincialTax(provincialTax);
        taxCalculation.setNetIncome(netIncome);
        taxCalculation.setUser(user);
        taxCalculation.setCalculationDate(LocalDateTime.now());
        taxCalculation.setSaved(false);  // Whether to save the calculation to tax history
        taxCalculation.setDeleted(false);  // Ensure it's not marked as deleted

        return taxCalculationRepository.save(taxCalculation);
    }

    // Helper method to calculate tax based on tax brackets
    private BigDecimal calculateTaxForBrackets(BigDecimal income, List<TaxBracket> taxBrackets) {
        BigDecimal totalTax = BigDecimal.ZERO;

        for (TaxBracket bracket : taxBrackets) {
            System.out.println("Calculating tax for bracket: " + bracket);
            System.out.println("Income: " + income + ", Min: " + bracket.getMaxIncome() + ", Max: " + bracket.getMinIncome() + ", Rate: " + bracket.getTaxRate());

            BigDecimal minIncome = bracket.getMinIncome();
            BigDecimal maxIncome = bracket.getMaxIncome().compareTo(new BigDecimal("999999999.99")) == 0
                    ? income : bracket.getMaxIncome();  // Cap at user's income or bracket's max
            BigDecimal taxRate = bracket.getTaxRate();

            // Calculate tax only on the portion of income within the bracket range
            if (income.compareTo(minIncome) > 0) {
                BigDecimal taxableIncome = income.min(maxIncome).subtract(minIncome);
                totalTax = totalTax.add(taxableIncome.multiply(taxRate));
            }
        }

        return totalTax;
    }

    // Soft-delete a tax calculation
    public void softDeleteTaxCalculation(Long calculationId) {
        TaxCalculation taxCalculation = taxCalculationRepository.findByIdAndDeletedFalse(calculationId)
                .orElseThrow(() -> new ResourceNotFoundException("Calculation not found"));

        taxCalculation.setDeleted(true);  // Soft delete
        taxCalculationRepository.save(taxCalculation);
    }

    // Get all tax calculations for a user that are not deleted
    public List<TaxCalculation> getAllForUser(User user) {
        return taxCalculationRepository.findByUserAndDeletedFalse(user);
    }

    // Find a tax calculation by ID
    public Optional<TaxCalculation> findById(Long calculationId) {
        return taxCalculationRepository.findByIdAndDeletedFalse(calculationId);
    }

}
