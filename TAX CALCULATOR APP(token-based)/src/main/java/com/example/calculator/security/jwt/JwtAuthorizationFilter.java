package com.example.calculator.security.jwt;

import com.example.calculator.security.CustomUserDetails;
import com.example.calculator.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final CustomUserDetailsService userDetailsService;  // Adjusted to use CustomUserDetailsService
    private final JwtUtils jwtUtils;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtils jwtUtils) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String requestURI = request.getRequestURI();
//        String header = request.getHeader("Authorization");
//
//        // Log the request and headers for debugging
//        System.out.println("Request for: " + requestURI);
//        System.out.println("Authorization header: " + header);
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            System.out.println("Missing or invalid Authorization header");
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.getWriter().write("Access denied - missing or invalid JWT");
//            return;
//        }
//
//        String token = header.replace("Bearer ", "");
//        if (!jwtUtils.validateToken(token)) {
//            System.out.println("Token validation failed");
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.getWriter().write("Access denied - invalid or expired JWT");
//            return;
//        }
//
//        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
//        if (authentication == null) {
//            System.out.println("Authentication failed");
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.getWriter().write("Access denied - authentication failed");
//            return;
//        }
//
//        // Set authentication and continue
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        chain.doFilter(request, response);
//    }




//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String requestURI = request.getRequestURI();
//
//        // List of static resources that require authentication
//        if (requestURI.endsWith(".html") && isProtectedResource(requestURI)) {
//            String header = request.getHeader("Authorization");
//
//            // If no Authorization header or it doesn't start with "Bearer ", return 403 Forbidden
//            if (header == null || !header.startsWith("Bearer ")) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write("Access denied - missing or invalid JWT");
//                return;
//            }
//
//            String token = header.replace("Bearer ", "");
//
//            // Validate token and set user authentication
//            UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
//            if (authentication == null) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write("Access denied - invalid or expired JWT");
//                return;
//            }
//
//            // Set authentication if valid
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        // Continue with the filter chain
//        chain.doFilter(request, response);
//    }

    // Helper method to determine if the resource is protected
    private boolean isProtectedResource(String requestURI) {
        // Customize the protected resources (can add more protected pages here)
        return requestURI.endsWith("account.html") ||
                requestURI.endsWith("calculate-tax.html") ||
                requestURI.endsWith("tax-history.html");
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (token != null && jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);
            Long userId = jwtUtils.getUserIdFromToken(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails instanceof CustomUserDetails customUserDetails) {
                if (!customUserDetails.getUserId().equals(userId)) {
                    throw new IllegalArgumentException("Token userId does not match stored userId");
                }
            }

            // Log successful authentication
            System.out.println("Authenticated user: " + username);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            System.out.println("Token validation failed for token: " + token);
        }
        return null;
    }


//    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
//        // Validate the token and extract the username and userId
//        if (token != null && jwtUtils.validateToken(token)) {
//            String username = jwtUtils.getUsernameFromToken(token);  // Extract username from token
//            Long userId = jwtUtils.getUserIdFromToken(token);  // Extract userId from token
//
//            // Load user details using the extracted username
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            // Ensure that userDetails is of type CustomUserDetails (optional, but for extra safety)
//            if (userDetails instanceof CustomUserDetails customUserDetails) {
//
//                if (!customUserDetails.getUserId().equals(userId)) {
//                    throw new IllegalArgumentException("Token userId does not match stored userId");
//                }
//            }
//
//            // Return authenticated token with user details
//            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        }
//        return null;
//    }
}
