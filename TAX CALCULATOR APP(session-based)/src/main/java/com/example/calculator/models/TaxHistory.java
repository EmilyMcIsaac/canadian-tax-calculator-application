package com.example.calculator.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tax_history")
public class TaxHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "calculation_id", nullable = false)
    private TaxCalculation taxCalculation;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;

    public TaxHistory() {}

    // Getters and setters

    public Long getId() {
        return historyId;
    }

    public void setId(Long id) {
        this.historyId = id;
    }

    public TaxCalculation getTaxCalculation() {
        return taxCalculation;
    }

    public void setTaxCalculation(TaxCalculation taxCalculation) {
        this.taxCalculation = taxCalculation;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}
