package com.example.calculator.controllers;

import com.example.calculator.dto.LoginRequest;
import com.example.calculator.models.User;
import com.example.calculator.services.UserService;
import com.example.calculator.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;  // Assuming you have a service to fetch user details

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Get the authenticated user details
            User user = userService.findByUsernameOrEmail(loginRequest.getUsernameOrEmail());

            // Generate JWT token with both username and userId
            String token = jwtUtils.generateToken(user.getUsername(), user.getId());  // Pass userId along with username

            // Log the successful login
            logger.info("Authentication successful for user: " + user.getUsername());

            // Return token and userId in the response
            return ResponseEntity.ok().body(Map.of("token", token, "message", "Login successful"));
        } catch (AuthenticationException e) {
            // Return error in JSON format
            logger.error("Authentication failed for user: " + loginRequest.getUsernameOrEmail());
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
    }
}
