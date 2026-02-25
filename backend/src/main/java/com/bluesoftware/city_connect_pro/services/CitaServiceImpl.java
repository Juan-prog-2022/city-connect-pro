package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.Cita;
import com.bluesoftware.city_connect_pro.repositories.CitaRepository;

@Service
public class CitaServiceImpl implements CitaService{

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> buscarCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    @Override
    public Cita guardarCita(Cita cita) {
        return citaRepository.save(cita);
    }

    @Override
    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }

    @Override
    public List<Cita> buscarPorProfesional(Long idProfesional) {
        return citaRepository.findByProfesionalId(idProfesional);
    }

    @Override
    public List<Cita> buscarPorUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }

}
