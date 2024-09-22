package com.example.calculator.services;

import com.example.calculator.models.BasicPersonalAmount;
import com.example.calculator.repositories.BasicPersonalAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BPAService {

    @Autowired
    private BasicPersonalAmountRepository bpaRepository;

    // Fetch BPA based on region, year, and income
    public BigDecimal getBpa(String region, int year, BigDecimal income) {
        Optional<BasicPersonalAmount> optionalBpa = bpaRepository.findByRegionAndYear(region, year);

        if (!optionalBpa.isPresent()) {
            throw new RuntimeException("BPA not found for region: " + region + " and year: " + year);
        }

        BasicPersonalAmount bpa = optionalBpa.get();

        // Handle BPA phaseout if applicable
        if (bpa.getBpaPhaseoutStart() != null && bpa.getBpaPhaseoutEnd() != null && income.compareTo(bpa.getBpaPhaseoutStart()) > 0) {
            BigDecimal phaseoutRange = bpa.getBpaPhaseoutEnd().subtract(bpa.getBpaPhaseoutStart());
            BigDecimal reduction = income.subtract(bpa.getBpaPhaseoutStart())
                    .divide(phaseoutRange, 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(bpa.getBpaAmount());
            return bpa.getBpaAmount().subtract(reduction).max(BigDecimal.ZERO);
        }

        // Return full BPA if no phaseout
        return bpa.getBpaAmount();
    }
}
