package com.bluesoftware.city_connect_pro.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluesoftware.city_connect_pro.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Login (username o email)
    Optional<User> findByUsernameOrEmail(String username, String email);

    // 🔍 Búsquedas individuales
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByDni(String dni);

    // 🔒 Validaciones
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}