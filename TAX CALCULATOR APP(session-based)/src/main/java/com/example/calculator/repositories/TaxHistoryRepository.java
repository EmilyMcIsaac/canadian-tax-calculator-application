package com.example.calculator.repositories;

import com.example.calculator.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxHistoryRepository extends JpaRepository<TaxHistory, Long> {

    // Find all tax history entries for a specific tax calculation
    List<TaxHistory> findByTaxCalculation(TaxCalculation taxCalculation);

    // Find all tax history entries for a specific user through their tax calculations
    @Query("SELECT th FROM TaxHistory th JOIN th.taxCalculation tc WHERE tc.user = :user")
    List<TaxHistory> findByUser(User user);
}
