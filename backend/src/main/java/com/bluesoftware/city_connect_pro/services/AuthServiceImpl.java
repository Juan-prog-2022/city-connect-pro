package com.bluesoftware.city_connect_pro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.dtos.*;
import com.bluesoftware.city_connect_pro.entities.Role;
import com.bluesoftware.city_connect_pro.entities.RoleName;
import com.bluesoftware.city_connect_pro.entities.User;
import com.bluesoftware.city_connect_pro.repositories.RoleRepository;
import com.bluesoftware.city_connect_pro.repositories.UserRepository;
import com.bluesoftware.city_connect_pro.security.JwtService;

import jakarta.persistence.EntityNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // 🔒 Validaciones
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        if (userRepository.existsByDni(request.getDni())) {
            throw new IllegalStateException("DNI already exists");
        }

        // 🔍 Rol USER obligatorio
        Role roleUser = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("ROLE_USER not found"));

        // 🧱 Crear usuario
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDni(request.getDni());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setUsername(request.getUsername().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRoles(Set.of(roleUser));
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String value = request.getUsernameOrEmail().trim().toLowerCase();

        User user = userRepository.findByUsernameOrEmail(value, value)
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));

        // 🔐 validar password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Invalid credentials");
        }

        // 🎫 generar roles
        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .username(user.getUsername())
                .userId(user.getId())
                .roles(roles)
                .build();
    }
}
