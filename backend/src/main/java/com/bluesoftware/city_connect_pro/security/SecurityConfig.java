package com.bluesoftware.city_connect_pro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bluesoftware.city_connect_pro.filter.JwtAuthenticationFilter;
import com.bluesoftware.city_connect_pro.filter.JwtAuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authz) -> authz
                        // Rutas públicas
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/registrarse").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Profesionales - acceso público para GET (listar y buscar)
                        .requestMatchers(HttpMethod.GET, "/api/profesionales/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/profesionales").authenticated()
                        // Profesionales - solo ADMIN puede actualizar, eliminar
                        .requestMatchers(HttpMethod.PUT, "/api/profesionales/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/profesionales/**").hasRole("ADMIN")

                        // Usuarios - autenticación requerida para ver y modificar
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").authenticated()

                        // Citas - autenticación requerida para ver y modificar
                        .requestMatchers(HttpMethod.GET, "/api/citas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/citas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/citas/**").authenticated()

                        // Usuarios - solo ADMIN puede eliminar usuarios y crear usuarios directamente
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMIN")
                        // 🔹 Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated())

                // 🔹 Configurar autenticación basada en token
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter( new JwtAuthorizationFilter(authenticationManager()))

                // 🔹 Deshabilitar CSRF (porque estamos usando autenticación basada en tokens)
                .csrf(csrf -> csrf.disable())

                // 🔹 Configurar sesión sin estado (para API REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .build();
    }

}
