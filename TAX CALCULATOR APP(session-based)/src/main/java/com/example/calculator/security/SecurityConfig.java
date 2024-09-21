package com.example.calculator.security;


import com.example.calculator.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        // Public access to login and register pages
                        .requestMatchers("/login.html", "/register.html", "homepage.html").permitAll()
                        .requestMatchers("/api/auth/**", "/api/users/register", "/api/auth/login").permitAll()

                        // All other requests require authentication
                        .requestMatchers("/account.html", "/calculate-tax.html", "/tax-history.html").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login.html") // Custom login page
                        .defaultSuccessUrl("/account.html", true) // Redirect after successful login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create a session if one doesn't exist
                )
                .build();
    }

}



//package com.example.calculator.security;
//
//import com.example.calculator.security.jwt.JwtUtils;
//import com.example.calculator.services.CustomUserDetailsService;
//import com.example.calculator.security.jwt.JwtAuthenticationFilter;
//import com.example.calculator.security.jwt.JwtAuthorizationFilter;
//import com.example.calculator.services.UserService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final CustomUserDetailsService customUserDetailsService;
//    private final JwtUtils jwtUtils;
//    private final UserService userService;  // Add UserService here
//
//    // Constructor injection for UserService
//    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtils jwtUtils, UserService userService) {
//        this.customUserDetailsService = customUserDetailsService;
//        this.jwtUtils = jwtUtils;
//        this.userService = userService;  // Inject the UserService here
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
////                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))  // Disable CSRF for API paths
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/", "/homepage.html", "/login.html", "/register.html", "/index.html").permitAll()
//                        .requestMatchers("/api/auth/**", "/api/users/register", "/api/auth/login").permitAll()
//                        .requestMatchers("/api/tax-calculations/calculate").hasRole("USER")
//                        .requestMatchers("/account.html", "/calculate-tax.html", "/tax-history.html").authenticated()
//                        .anyRequest().authenticated())
//                // Use addFilterBefore to insert JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter
//                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtils, userService), UsernamePasswordAuthenticationFilter.class)
//                // Use addFilterBefore for JwtAuthorizationFilter as well
//                .addFilterBefore(new JwtAuthorizationFilter(authenticationManager, customUserDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class);
//
//
//        return http.build();
//    }
//
//}
