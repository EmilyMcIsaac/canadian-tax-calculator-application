package com.example.calculator.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "basic_personal_amounts")
public class BasicPersonalAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bpa_id")
    private Long bpaId;

    @Column(nullable = false)
    private String region; // federal or province

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private BigDecimal bpaAmount;

    @Column
    private BigDecimal bpaPhaseoutStart; // Optional, can be null for provinces without phaseout

    @Column
    private BigDecimal bpaPhaseoutEnd; // Optional, can be null for provinces without phaseout

    public BasicPersonalAmount() {}
    // Getters and Setters
    public Long getId() {
        return bpaId;
    }

    public void setId(Long id) {
        this.bpaId = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getBpaAmount() {
        return bpaAmount;
    }

    public void setBpaAmount(BigDecimal bpaAmount) {
        this.bpaAmount = bpaAmount;
    }

    public BigDecimal getBpaPhaseoutStart() {
        return bpaPhaseoutStart;
    }

    public void setBpaPhaseoutStart(BigDecimal bpaPhaseoutStart) {
        this.bpaPhaseoutStart = bpaPhaseoutStart;
    }

    public BigDecimal getBpaPhaseoutEnd() {
        return bpaPhaseoutEnd;
    }

    public void setBpaPhaseoutEnd(BigDecimal bpaPhaseoutEnd) {
        this.bpaPhaseoutEnd = bpaPhaseoutEnd;
    }
}
