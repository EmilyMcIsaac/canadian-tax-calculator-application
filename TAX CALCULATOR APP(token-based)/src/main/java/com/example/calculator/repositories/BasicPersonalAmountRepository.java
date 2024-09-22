package com.example.calculator.repositories;


import com.example.calculator.models.BasicPersonalAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BasicPersonalAmountRepository extends JpaRepository<BasicPersonalAmount, Long> {

    // Fetch the BPA based on region and year
    Optional<BasicPersonalAmount> findByRegionAndYear(String region, int year);
}
