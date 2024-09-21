package com.example.calculator.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tax_brackets")
public class TaxBracket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bracket_id")
    private Long bracketId;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "min_income", nullable = false)
    private BigDecimal minIncome;

    @Column(name = "max_income", nullable = false)
    private BigDecimal maxIncome;

    @Column(name = "tax_rate", nullable = false)
    private BigDecimal taxRate;

    public TaxBracket(String region, Integer year, BigDecimal minIncome, BigDecimal maxIncome, BigDecimal taxRate) {
        this.region = region;
        this.year = year;
        this.minIncome = minIncome;
        this.maxIncome = maxIncome;
        this.taxRate = taxRate;

    }

    public TaxBracket() {}

    // Getters and setters

    public Long getId() {
        return bracketId;
    }

    public void setId(Long id) {
        this.bracketId = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getMinIncome() {
        return minIncome;
    }

    public void setMinIncome(BigDecimal minIncome) {
        this.minIncome = minIncome;
    }

    public BigDecimal getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(BigDecimal maxIncome) {
        this.maxIncome = maxIncome;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}
