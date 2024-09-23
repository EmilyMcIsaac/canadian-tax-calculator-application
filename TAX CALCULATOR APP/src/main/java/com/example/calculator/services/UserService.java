package com.example.calculator.services;

import com.example.calculator.dto.UserDTO;
import com.example.calculator.models.User;
import com.example.calculator.repositories.UserRepository;
import com.example.calculator.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor Injection
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user and return the persisted User entity
    public User registerUser(UserDTO userDTO) {
        // Create a new User entity from the UserDTO
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(userDTO.getPassword())); // Encode the password

        // Save the user in the repository and return the persisted user
        return userRepository.save(newUser);
    }

    // Check if raw password matches the encoded password in the database
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    // Find user by ID or throw exception if not found
    public User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateUser(User updatedUser, String oldPassword, String newPassword) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Update email and username
            user.setEmail(updatedUser.getEmail());
            user.setUsername(updatedUser.getUsername());

            // Check if password update is requested
            if (oldPassword != null && newPassword != null) {
                if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                    // Update password if old password matches
                    user.setPasswordHash(passwordEncoder.encode(newPassword));
                } else {
                    throw new IllegalArgumentException("Old password does not match");
                }
            }

            return userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    // Delete a user account
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
