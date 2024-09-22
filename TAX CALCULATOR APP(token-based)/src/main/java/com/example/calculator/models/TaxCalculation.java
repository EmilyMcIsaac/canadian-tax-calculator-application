package com.example.calculator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tax_calculations")
public class TaxCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculation_id")
    private Long calculationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal income;

    @Column(nullable = false)
    private String region;

    @Column(name = "federal_tax", nullable = false)
    private BigDecimal federalTax;

    @Column(name = "provincial_tax", nullable = false)
    private BigDecimal provincialTax;

    @Column(name = "net_income", nullable = false)
    private BigDecimal netIncome;

    @Column(name = "calculation_date", nullable = false)
    private LocalDateTime calculationDate;

    @Column(nullable = false)
    private Boolean saved;

    @Column(nullable = false)
    private Boolean deleted;

    // New taxYear field
    @Column(name = "tax_year", nullable = false)
    private int taxYear;

    @JsonIgnore
    @OneToMany(mappedBy = "taxCalculation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaxHistory> taxHistories;

    // Constructor
    public TaxCalculation(User user, BigDecimal income, String region, int taxYear, BigDecimal provincialTax,
                          BigDecimal federalTax, BigDecimal netIncome, LocalDateTime calculationDate, boolean saved) {
        this.user = user;
        this.income = income;
        this.region = region;
        this.taxYear = taxYear;
        this.provincialTax = provincialTax;
        this.federalTax = federalTax;
        this.netIncome = netIncome;
        this.calculationDate = calculationDate;
        this.saved = saved;
    }

    public TaxCalculation() {

    }

    // Getters and setters

    public Long getId() {
        return calculationId;
    }

    public void setId(Long id) {
        this.calculationId = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getNetFederalTax() {
        return federalTax;
    }

    public void setNetFederalTax(BigDecimal netFederalTax) {
        this.federalTax = netFederalTax;
    }

    public BigDecimal getNetProvincialTax() {
        return provincialTax;
    }

    public void setNetProvincialTax(BigDecimal netProvincialTax) {
        this.provincialTax = netProvincialTax;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }

    public LocalDateTime getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDateTime calculationDate) {
        this.calculationDate = calculationDate;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<TaxHistory> getTaxHistories() {
        return taxHistories;
    }

    public void setTaxHistories(List<TaxHistory> taxHistories) {
        this.taxHistories = taxHistories;
    }

    public int getTaxYear() {
        return taxYear;
    }
    public void setTaxYear(int taxYear) {
        this.taxYear = taxYear;
    }

}
