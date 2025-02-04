package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import com.bluesoftware.city_connect_pro.entities.Cita;

public interface CitaService {
    List<Cita> listarCitas();

    Optional<Cita> buscarCitaPorId(Long id);

    Cita guardarCita(Cita cita);

    void eliminarCita(Long id);

    List<Cita> buscarPorProfesional(Long idProfesional);
    
    List<Cita> buscarPorUsuario(Long usuarioId);

}
