package com.example.calculator.security;

import com.example.calculator.security.jwt.JwtUtils;
import com.example.calculator.services.CustomUserDetailsService;
import com.example.calculator.security.jwt.JwtAuthenticationFilter;
import com.example.calculator.security.jwt.JwtAuthorizationFilter;
import com.example.calculator.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtils, UserService userService) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/homepage.html", "/login.html", "/register.html", "/index.html").permitAll()
                        .requestMatchers("/api/auth/**", "/api/users/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/tax-calculations/calculate").permitAll()
                        .requestMatchers("/account.html", "/calculate-tax.html", "/tax-history.html", "logout.html", "/account", "/calculate-tax", "/tax-history").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtils, userService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager, customUserDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
