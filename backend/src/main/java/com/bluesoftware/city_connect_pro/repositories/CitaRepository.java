package com.bluesoftware.city_connect_pro.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluesoftware.city_connect_pro.entities.Cita;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    
    // 🔍 Buscar citas por el ID del profesional
    List<Cita> findByProfesionalId(Long profesionalId);

    // 🔍 Buscar citas por el ID del usuario
    List<Cita> findByUsuarioId(Long usuarioId);

}
