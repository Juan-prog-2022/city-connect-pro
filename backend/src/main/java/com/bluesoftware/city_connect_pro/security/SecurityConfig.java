package com.bluesoftware.city_connect_pro.security;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bluesoftware.city_connect_pro.repositories.UserRepository;
import com.bluesoftware.city_connect_pro.security.filters.JwtAuthorizationFilter;
import com.bluesoftware.city_connect_pro.security.filters.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity 
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired    
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                frontendUrl,
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "https://city-connect-pro.vercel.app"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager
    ) throws Exception {

        return http

                // =========================================
                // CSRF
                // =========================================

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                                "/api/auth/**",
                                "/api/webhooks/**"
                        ).permitAll()

                        // SWAGGER / OPENAPI (springdoc)
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // OPTIONS (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // EVERYTHING ELSE
                        .anyRequest()
                        .authenticated()
                )
                
                .authenticationManager(authenticationManager)
                .addFilterBefore(new JwtAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtService, userRepository))
                .build();
    }
}
