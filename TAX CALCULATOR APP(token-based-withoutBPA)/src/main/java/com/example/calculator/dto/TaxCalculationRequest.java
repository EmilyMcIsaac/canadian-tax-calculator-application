package com.example.calculator.dto;

import java.math.BigDecimal;

public class TaxCalculationRequest {

    private BigDecimal income;
    private String province;
    private Long userId;

    public BigDecimal getIncome() {
        return income;
    }
    public void setIncome(BigDecimal income) {
        this.income = income;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
