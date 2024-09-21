//package com.example.calculator.services;
//
//import com.example.calculator.exception.ResourceNotFoundException;
//import com.example.calculator.models.*;
//import com.example.calculator.repositories.TaxBracketRepository;
//import com.example.calculator.repositories.TaxCalculationRepository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class TestCalculationServiceTest {
//
//    @Mock
//    private TaxCalculationRepository taxCalculationRepository;
//
//    @Mock
//    private TaxBracketRepository taxBracketRepository;
//
//    @InjectMocks
//    private TaxCalculationService taxCalculationService;
//
//    private User testUser;
//    private List<TaxBracket> federalBrackets;
//    private List<TaxBracket> provincialBrackets;
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize test user
//        testUser = new User();
//        testUser.setUsername("testuser");
//        testUser.setEmail("testuser@example.com");
//
//        // Initialize test tax brackets
//        federalBrackets = Arrays.asList(
//                new TaxBracket("federal", 2024, new BigDecimal("0"), new BigDecimal("50000"), new BigDecimal("0.15")),
//                new TaxBracket("federal", 2024, new BigDecimal("50000"), new BigDecimal("100000"), new BigDecimal("0.20"))
//        );
//
//        provincialBrackets = Arrays.asList(
//                new TaxBracket("Ontario", 2024, new BigDecimal("0"), new BigDecimal("50000"), new BigDecimal("0.10")),
//                new TaxBracket("Ontario", 2024, new BigDecimal("50000"), new BigDecimal("100000"), new BigDecimal("0.13"))
//        );
//    }
//
//    @Test
//    public void testCreateTaxCalculation() {
//        // Mock the bracket repository to return tax brackets
//        when(taxBracketRepository.findByRegionAndYear("federal", 2024)).thenReturn(federalBrackets);
//        when(taxBracketRepository.findByRegionAndYear("Ontario", 2024)).thenReturn(provincialBrackets);
//
//        BigDecimal income = new BigDecimal("75000");
//
//        // Mock save to return the created TaxCalculation
//        TaxCalculation taxCalculation = new TaxCalculation();
//        taxCalculation.setIncome(income);
//        taxCalculation.setNetFederalTax(new BigDecimal("5000"));
//        taxCalculation.setNetProvincialTax(new BigDecimal("3750"));
//        taxCalculation.setNetIncome(new BigDecimal("66250"));
//        taxCalculation.setCalculationDate(LocalDateTime.now());
//        when(taxCalculationRepository.save(any(TaxCalculation.class))).thenReturn(taxCalculation);
//
//        // Call the method under test
//        TaxCalculation result = taxCalculationService.createTaxCalculation(income, "Ontario", testUser);
//
//        // Verify interactions
//        verify(taxBracketRepository, times(1)).findByRegionAndYear("federal", 2024);
//        verify(taxBracketRepository, times(1)).findByRegionAndYear("Ontario", 2024);
//        verify(taxCalculationRepository, times(1)).save(any(TaxCalculation.class));
//
//        // Assert results
//        assertNotNull(result);
//        assertEquals(income, result.getIncome());
//        assertEquals(new BigDecimal("5000"), result.getNetFederalTax());
//        assertEquals(new BigDecimal("3750"), result.getNetProvincialTax());
//        assertEquals(new BigDecimal("66250"), result.getNetIncome());
//    }
//
//    @Test
//    public void testDeleteTaxCalculation() {
//        // Mock a TaxCalculation
//        TaxCalculation calculation = new TaxCalculation();
//        calculation.setId(1L);
//        calculation.setDeleted(false);
//
//        // Mock findById to return the TaxCalculation
//        when(taxCalculationRepository.findById(1L)).thenReturn(Optional.of(calculation));
//
//        // Call the method under test
//        taxCalculationService.deleteTaxCalculation(1L);
//
//        // Verify that the calculation was soft deleted
//        assertTrue(calculation.getDeleted());
//
//        // Verify save was called
//        verify(taxCalculationRepository, times(1)).save(calculation);
//    }
//
//    @Test
//    public void testDeleteTaxCalculation_NotFound() {
//        // Mock findById to return empty (no calculation found)
//        when(taxCalculationRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Call the method and assert the exception
//        assertThrows(ResourceNotFoundException.class, () -> taxCalculationService.deleteTaxCalculation(1L));
//
//        // Verify that save was not called
//        verify(taxCalculationRepository, never()).save(any(TaxCalculation.class));
//    }
//}
