package com.example.calculator.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private final Key secretKey;
    private final long jwtExpirationMs;

    // Constructor: initialize the secret key and expiration time
    public JwtUtils(@Value("${app.jwt.expirationMs}") long jwtExpirationMs) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generate the key internally
        this.jwtExpirationMs = jwtExpirationMs;
    }

    // Generate a token with both username and userId as claims
    public String generateToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);  // Add userId to claims

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)  // Store the username in the "sub" claim (standard)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set the issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Set expiration
                .signWith(secretKey, SignatureAlgorithm.HS512)  // Sign with the secret key
                .compact();
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // Validate with the secret key
                    .build()
                    .parseClaimsJws(token);  // Check the token’s signature and claims
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log the reason for failure for debugging
            System.out.println("JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    // Extract the username from the token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  // Use the secret key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Extract the username from the "sub" (subject) claim
    }

    // Extract the userId from the token
    public Long getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  // Use the secret key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);  // Extract userId from the custom "userId" claim
    }
}
