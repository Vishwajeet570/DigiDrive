package com.digidrive.document_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/test/**").permitAll()   // Public test endpoint
                .requestMatchers("/auth/**").permitAll()   // Public auth endpoints
                .anyRequest().authenticated()              // All other endpoints require auth
            )
            .httpBasic(httpBasic -> {})  // Optional: use HTTP Basic for testing
            .formLogin(form -> form.disable());  // Disable default login form

        return http.build();
    }
}
