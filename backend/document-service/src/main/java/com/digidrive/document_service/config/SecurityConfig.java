// package com.digidrive.document_service.config;

// import com.digidrive.document_service.security.JwtAuthenticationFilter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//     private final JwtAuthenticationFilter jwtFilter;

//     public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
//         this.jwtFilter = jwtFilter;
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//         http.csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers(
//                                 "/auth/**",
//                                 "/test/public",
//                                 "/actuator/**"
//                         ).permitAll()
//                         .anyRequest().authenticated()
//                 )
//                 .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }

package com.digidrive.document_service.config;

import com.digidrive.document_service.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                        "/auth/**",
                        "/test/public",
                        "/actuator/**"
                ).permitAll()

                // Role-based protected endpoints
                .requestMatchers("/test/admin").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/test/police").hasAuthority("ROLE_POLICE")
                .requestMatchers("/test/user").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/vehicle/register").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/vehicle/my").hasAuthority("ROLE_USER")
                .requestMatchers("/vehicle/all").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/vehicle/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_POLICE")
                .requestMatchers("/documents/upload/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .requestMatchers("/documents/vehicle/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN","ROLE_POLICE")
                .requestMatchers("/documents/*/download").hasAnyAuthority("ROLE_USER","ROLE_ADMIN","ROLE_POLICE")
                .requestMatchers("/documents/*/status").hasAuthority("ROLE_ADMIN")
                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            // Add JWT filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            // Disable the default /login page
            .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

