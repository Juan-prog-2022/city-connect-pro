package com.bluesoftware.city_connect_pro.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http

                // =========================================
                // CSRF
                // =========================================

                .csrf(csrf -> csrf.disable())

                // =========================================
                // SESSION
                // =========================================

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // =========================================
                // AUTHORIZATION
                // =========================================

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC ENDPOINTS
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // SWAGGER
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ACTUATOR
                        .requestMatchers(
                                "/actuator/**"
                        ).permitAll()

                        // OPTIONS (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // EVERYTHING ELSE
                        .anyRequest()
                        .authenticated()
                )

                // =========================================
                // BASIC AUTH (TEMPORARY)
                // =========================================

                .httpBasic(Customizer.withDefaults())

                .build();
    }
}