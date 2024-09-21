package com.example.calculator.controllers;

import com.example.calculator.dto.UserDTO;
import com.example.calculator.models.User;
import com.example.calculator.services.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed", "details", errors));
        }

        Optional<User> existingUser = userService.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        Optional<User> existingEmailUser = userService.findByEmail(userDTO.getEmail());
        if (existingEmailUser.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        try {
            User newUser = userService.registerUser(userDTO);

            // Return a success message as JSON
            return ResponseEntity.ok(Map.of("message", "Registration successful", "userId", newUser.getId()));
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed", "details", e.getMessage()));
        }
    }


    // Get user details by username
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Update user information
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO updatedUserDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .toList());
        }

        try {
            User existingUser = userService.findByIdOrThrow(userId);
            existingUser.setUsername(updatedUserDTO.getUsername());
            existingUser.setEmail(updatedUserDTO.getEmail());
            userService.updateUser(existingUser);

            return ResponseEntity.ok(existingUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + e.getMessage());
        }
    }

    // Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Get current authenticated user
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
        }

        String currentUsername = authentication.getName();
        logger.info("Fetching user with username: {}", currentUsername);

        Optional<User> userOptional = userService.findByUsername(currentUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(user.getUsername(), user.getEmail());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
    }

    // Check if the user session is valid
    @GetMapping("/checkSession")
    public ResponseEntity<?> checkSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session invalid or user not authenticated.");
        }

        return ResponseEntity.ok("User is authenticated: " + auth.getName());
    }

    // Access a protected endpoint
    @GetMapping("/protected-endpoint")
    public ResponseEntity<?> getProtectedResource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
        }

        return ResponseEntity.ok("You are accessing a protected resource.");
    }
}




//package com.example.calculator.controllers;
//
//import com.example.calculator.dto.UserDTO;
//import com.example.calculator.models.User;
//import com.example.calculator.security.jwt.JwtUtils;
//import com.example.calculator.services.UserService;
//
//import jakarta.validation.Valid;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    Logger logger = LoggerFactory.getLogger(UserController.class);
//
//    private final JwtUtils jwtUtils;
//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService, JwtUtils jwtUtils) {
//        this.userService = userService;
//        this.jwtUtils = jwtUtils;
//    }
//
//    // Register a new user
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
//        if (result.hasErrors()) {
//            List<String> errors = result.getFieldErrors().stream()
//                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                    .toList();
//            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed", "details", errors));
//        }
//
//        Optional<User> existingUser = userService.findByUsername(userDTO.getUsername());
//        if (existingUser.isPresent()) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
//        }
//
//        Optional<User> existingEmailUser = userService.findByEmail(userDTO.getEmail());
//        if (existingEmailUser.isPresent()) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
//        }
//
//        try {
//            // Register the new user and retrieve the persisted user entity with the userId
//            User newUser = userService.registerUser(userDTO);
//
//            // Generate JWT token for the newly registered user including userId in the claims
//            String token = jwtUtils.generateToken(newUser.getUsername(), newUser.getId());  // Pass userId
//
//            // Return JWT token, userId, and success message
//            return ResponseEntity.ok(Map.of("message", "Registration successful", "token", token, "userId", newUser.getId()));
//        } catch (Exception e) {
//            logger.error("Error during user registration", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Registration failed", "details", e.getMessage()));
//        }
//    }
//
//        // Get user details by username
//    @GetMapping("/{username}")
//    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
//        Optional<User> user = userService.findByUsername(username);
//        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//    // Update user information
//    @PutMapping("/{userId}")
//    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO updatedUserDTO, BindingResult result) {
//        if (result.hasErrors()) {
//            return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
//                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
//                    .toList());
//        }
//
//        try {
//            User existingUser = userService.findByIdOrThrow(userId);
//            existingUser.setUsername(updatedUserDTO.getUsername());
//            existingUser.setEmail(updatedUserDTO.getEmail());
//            userService.updateUser(existingUser);
//
//            return ResponseEntity.ok(existingUser);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed: " + e.getMessage());
//        }
//    }
//
//    // Delete user
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        userService.deleteUser(userId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/current")
//    public ResponseEntity<?> getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
//            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
//        }
//
//        String currentUsername = authentication.getName();
//        logger.info("Fetching user with username: {}", currentUsername);
//
//        Optional<User> userOptional = userService.findByUsername(currentUsername);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            UserDTO userDTO = new UserDTO(user.getUsername(), user.getEmail());
//            return ResponseEntity.ok(userDTO);
//        } else {
//            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
//        }
//    }
//
//
//    @GetMapping("/checkSession")
//    public ResponseEntity<?> checkSession() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session invalid or user not authenticated.");
//        }
//
//        return ResponseEntity.ok("User is authenticated: " + auth.getName());
//    }
//
//    @GetMapping("/protected-endpoint")
//    public ResponseEntity<?> getProtectedResource() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access"));
//        }
//
//        return ResponseEntity.ok("You are accessing a protected resource.");
//    }
//}
//
