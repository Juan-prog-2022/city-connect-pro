package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.Especialidad;
import com.bluesoftware.city_connect_pro.entities.Profesional;
import com.bluesoftware.city_connect_pro.repositories.ProfesionalRepository;

@Service
public class ProfesionalServiceImpl implements ProfesionalService {

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Override
    public List<Profesional> listarProfesionales() {
        return profesionalRepository.findAll();
    }

    @Override
    public Optional<Profesional> buscarPorId(Long id) {
        return profesionalRepository.findById(id);
    }

    @Override
    public Profesional guardarProfesional(Profesional profesional) {
        return profesionalRepository.save(profesional);
    }

    @Override
    public List<Profesional> buscarPorEspecialidad(Especialidad especialidad) {
        return profesionalRepository.findByEspecialidad(especialidad);
    }

    @Override
    public List<Profesional> buscarPorCiudad(String ciudad) {
        return profesionalRepository.findByCiudadIgnoreCase(ciudad);
    }

     @Override
    public Optional<Profesional> buscarPorDni(String dni) {
        return profesionalRepository.findByDni(dni);
    }

    @Override
    public List<Profesional> buscarPorEspecialidadYCiudad(Especialidad especialidad, String ciudad) {
        return profesionalRepository.buscarPorEspecialidadYCiudad(especialidad, ciudad);
    }

    @Override
    public void eliminarProfesional(Long id) {
        profesionalRepository.deleteById(id);
    }

    @Override
    public Optional<Profesional> buscarPorEmail(String email) {
        return profesionalRepository.findByEmail(email);
    }
}
