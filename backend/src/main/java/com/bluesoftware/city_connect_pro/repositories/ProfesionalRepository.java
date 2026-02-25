package com.bluesoftware.city_connect_pro.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bluesoftware.city_connect_pro.entities.Especialidad;
import com.bluesoftware.city_connect_pro.entities.Profesional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {

    Optional<Profesional> findByDni(String dni);

    Optional<Profesional> findByEmail(String email);

    // Buscar profesionales por especialidad
    List<Profesional> findByEspecialidad(Especialidad especialidad);

    // Buscar profesionales por ciudad (se asume que en Profesional hay un campo ciudad)
    List<Profesional> findByCiudadIgnoreCase(String ciudad);

    // Buscar profesionales por especialidad y ciudad
    @Query("SELECT p FROM Profesional p WHERE p.especialidad = :especialidad AND LOWER(p.ciudad) = LOWER(:ciudad)")
    List<Profesional> buscarPorEspecialidadYCiudad(@Param("especialidad") Especialidad especialidad,
                                                  @Param("ciudad") String ciudad);
}
