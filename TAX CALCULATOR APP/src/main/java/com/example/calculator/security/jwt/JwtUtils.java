package com.example.calculator.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key secretKey;
    private final long jwtExpirationMs;

    public JwtUtils(@Value("${app.jwt.expirationMs}") long jwtExpirationMs) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generate the key internally
        this.jwtExpirationMs = jwtExpirationMs;
    }

    // Generate a token with both username and userId as claims
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // Subject (username)
//                .claim("userId", getUserIdFromToken(username))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Set expiration
                .signWith(secretKey, SignatureAlgorithm.HS512)  // Sign with secret key
                .compact();
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // Validate with the secret key
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract username from the token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Use the generated secret key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Extract username from the token's subject
    }

    // Extract userId from the token
    public Long getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Use the generated secret key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);  // Extract userId from the claims
    }
}