package com.example.calculator.services;

import com.example.calculator.exception.ResourceNotFoundException;
import com.example.calculator.models.*;
import com.example.calculator.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    // Find all tax calculations by user
    public List<TaxCalculation> getAllCalculationsByUser(User user) {
        return taxCalculationRepository.findByUserAndDeletedFalse(user);
    }

    // Soft delete a tax calculation
    public void deleteTaxCalculation(Long calculationId) {
        TaxCalculation calculation = taxCalculationRepository.findById(calculationId)
                .orElseThrow(() -> new ResourceNotFoundException("Calculation not found"));
        calculation.setDeleted(true);
        taxCalculationRepository.save(calculation);
    }

    // Restore a deleted calculation (optional)
    public void restoreDeletedCalculation(Long calculationId) {
        TaxCalculation calculation = taxCalculationRepository.findById(calculationId)
                .orElseThrow(() -> new ResourceNotFoundException("Calculation not found"));
        calculation.setDeleted(false);
        taxCalculationRepository.save(calculation);
    }
}
