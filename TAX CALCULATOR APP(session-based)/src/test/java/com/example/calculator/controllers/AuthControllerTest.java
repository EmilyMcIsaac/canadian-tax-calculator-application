//package com.example.calculator.controllers;
//
//import com.example.calculator.models.User;
//import com.example.calculator.services.UserService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.Optional;
//
//@SpringBootTest
//public class AuthControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private AuthController authController;
//
//    @Test
//    public void testLoginSuccess() {
//        String usernameOrEmail = "testuser";
//        String password = "password";
//        User user = new User("testuser", "test@example.com", "$2a$10$....hashedPassword");
//
//        // Mock the findByUsernameOrEmail method
//        when(userService.findByUsernameOrEmail(usernameOrEmail)).thenReturn(Optional.of(user));
//
//        // Mock the checkPassword method
//        when(userService.checkPassword(password, user.getPasswordHash())).thenReturn(true);
//
//        // Call the login method
//        ResponseEntity<String> response = authController.login(usernameOrEmail, password);
//
//        // Verify the status code and response body
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Login successful", response.getBody());
//    }
//
//    @Test
//    public void testLoginFailure() {
//        String usernameOrEmail = "testuser";
//        String password = "wrongpassword";
//        User user = new User("testuser", "test@example.com", "$2a$10$....hashedPassword");
//
//        // Mock the findByUsernameOrEmail method
//        when(userService.findByUsernameOrEmail(usernameOrEmail)).thenReturn(Optional.of(user));
//
//        // Mock the checkPassword method to return false for wrong password
//        when(userService.checkPassword(password, user.getPasswordHash())).thenReturn(false);
//
//        // Call the login method
//        ResponseEntity<String> response = authController.login(usernameOrEmail, password);
//
//        // Verify the status code and response body for failed login
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//        assertEquals("Invalid credentials", response.getBody());
//    }
//}
