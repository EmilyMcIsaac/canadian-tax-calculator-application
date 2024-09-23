package com.example.calculator.services;

import com.example.calculator.models.User;
import com.example.calculator.repositories.UserRepository;
import com.example.calculator.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .map(user -> new CustomUserDetails(
                        user.getId(),                  // Pass userId
                        user.getUsername(),            // Pass username
                        user.getPasswordHash(),        // Pass password
                        Collections.emptyList()        // Adjust authorities as necessary
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

}
