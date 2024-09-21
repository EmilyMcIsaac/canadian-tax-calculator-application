package com.example.calculator.security.jwt;

import com.example.calculator.models.User;
import com.example.calculator.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.calculator.dto.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;  // Inject the UserService to fetch user details

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;  // Inject userService
        setFilterProcessesUrl("/api/auth/login");  // Custom login endpoint
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Retrieve the user details (which should include the userId)
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        // Use UserService to find the user by username
        Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());

        // Handle the case where the user is not found
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userDetails.getUsername()));

        // Generate JWT token with both username and userId
        String token = jwtUtils.generateToken(userDetails.getUsername());

        // Set token in the response header
        response.addHeader("Authorization", "Bearer " + token);

        // Set token in the response body
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + token + "\"}");

    }
}
