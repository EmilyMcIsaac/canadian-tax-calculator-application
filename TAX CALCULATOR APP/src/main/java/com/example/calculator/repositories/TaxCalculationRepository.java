package com.example.calculator.repositories;

import com.example.calculator.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxCalculationRepository extends JpaRepository<TaxCalculation, Long> {

    // Find all tax calculations for a given user
    List<TaxCalculation> findByUser(User user);

    // Find all tax calculations for a given user that are not deleted
    List<TaxCalculation> findByUserAndDeletedFalse(User user);

    // Find a specific tax calculation by ID that is not deleted
    Optional<TaxCalculation> findByIdAndDeletedFalse(Long id);
}

