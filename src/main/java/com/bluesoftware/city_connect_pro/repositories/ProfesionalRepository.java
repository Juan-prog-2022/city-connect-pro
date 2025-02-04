package com.bluesoftware.city_connect_pro.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bluesoftware.city_connect_pro.entities.Especialidad;
import com.bluesoftware.city_connect_pro.entities.Profesional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long>{

    // Buscar por especialidad
    @Query("SELECT p FROM Profesional p WHERE p.especialidad = :especialidad")
    List<Profesional> buscarPorEspecialidad(@Param("especialidad") Especialidad especialidad);

    // Buscar por ciudad (ignorando mayúsculas/minúsculas)
    @Query("SELECT p FROM Profesional p WHERE LOWER(p.ciudad) = LOWER(:ciudad)")
    List<Profesional> buscarPorCiudad(@Param("ciudad") String ciudad);

    // 🔹 Si necesitas filtrar por especialidad y ciudad al mismo tiempo:
    @Query("SELECT p FROM Profesional p WHERE p.especialidad = :especialidad AND LOWER(p.ciudad) = LOWER(:ciudad)")
    List<Profesional> buscarPorEspecialidadYCiudad(@Param("especialidad") Especialidad especialidad, @Param("ciudad") String ciudad);

}
