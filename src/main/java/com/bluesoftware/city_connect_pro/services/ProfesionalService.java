package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import com.bluesoftware.city_connect_pro.entities.Especialidad;
import com.bluesoftware.city_connect_pro.entities.Profesional;

public interface ProfesionalService {
    List<Profesional> listarProfesionales();
    Optional<Profesional> buscarPorId(Long id);
    Profesional guardarProfesional(Profesional profesional);
    void eliminarProfesional(Long id);
    List<Profesional> buscarPorEspecialidad(Especialidad especialidad);
    List<Profesional> buscarPorCiudad(String ciudad);
    List<Profesional> buscarPorEspecialidadYCiudad(Especialidad especialidad, String ciudad);
}
