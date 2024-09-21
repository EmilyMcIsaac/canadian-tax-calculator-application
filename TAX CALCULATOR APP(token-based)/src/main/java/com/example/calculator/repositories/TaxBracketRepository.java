package com.example.calculator.repositories;

import com.example.calculator.models.TaxBracket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxBracketRepository extends JpaRepository<TaxBracket, Long> {

    // Find all tax brackets for a specific region and year
    List<TaxBracket> findByRegionAndYear(String region, Integer year);
}
