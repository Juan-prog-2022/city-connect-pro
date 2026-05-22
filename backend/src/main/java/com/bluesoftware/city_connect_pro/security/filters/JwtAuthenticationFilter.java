package com.bluesoftware.city_connect_pro.security.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bluesoftware.city_connect_pro.dtos.LoginRequest;
import com.bluesoftware.city_connect_pro.entities.User;
import com.bluesoftware.city_connect_pro.repositories.UserRepository;
import com.bluesoftware.city_connect_pro.security.JwtConstants;
import com.bluesoftware.city_connect_pro.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        setFilterProcessesUrl(JwtConstants.LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Could not read login request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName();
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        Set<String> roles = authResult.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(user);

        response.addHeader(JwtConstants.AUTHORIZATION_HEADER, JwtConstants.TOKEN_PREFIX + token);
        response.setContentType(JwtConstants.CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("userId", user.getId());
        responseBody.put("username", username);
        responseBody.put("roles", roles);
        responseBody.put("message", String.format("Usuario '%s' autenticado con éxito", username));

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", "Unauthorized");
        responseBody.put("message", "Credenciales inválidas. Por favor, inténtalo de nuevo.");
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(JwtConstants.CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
