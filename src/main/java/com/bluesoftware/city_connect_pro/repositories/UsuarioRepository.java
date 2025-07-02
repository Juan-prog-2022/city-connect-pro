package com.bluesoftware.city_connect_pro.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluesoftware.city_connect_pro.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
