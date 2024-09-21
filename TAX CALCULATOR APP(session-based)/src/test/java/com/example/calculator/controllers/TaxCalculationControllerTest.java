//package com.example.calculator.controllers;
//
//import com.example.calculator.models.TaxCalculation;
//import com.example.calculator.models.User;
//import com.example.calculator.services.TaxCalculationService;
//import com.example.calculator.services.UserService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//@SpringBootTest
//public class TaxCalculationControllerTest {
//
//    @Mock
//    private TaxCalculationService taxCalculationService;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private TaxCalculationController taxCalculationController;
//
//    @Test
//    public void testCalculateTax() {
//        String province = "Ontario";
//        BigDecimal income = new BigDecimal("50000");
//        Long userId = 1L;
//        User user = new User("testuser", "test@example.com", "password");
//
//        TaxCalculation calculation = new TaxCalculation(user, income, province, new BigDecimal("5000"),
//                new BigDecimal("2500"), new BigDecimal("42500"),
//                null, true);
//
//        when(userService.findByIdOrThrow(userId)).thenReturn(user);
//        when(taxCalculationService.createTaxCalculation(any(TaxCalculation.class))).thenReturn(calculation);
//
//        ResponseEntity<TaxCalculation> response = taxCalculationController.calculateTax(income, province, userId);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Ontario", response.getBody().getRegion());
//    }
//}
