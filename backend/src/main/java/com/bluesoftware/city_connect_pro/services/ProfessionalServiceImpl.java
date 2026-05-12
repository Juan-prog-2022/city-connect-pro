package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.Professional;
import com.bluesoftware.city_connect_pro.repositories.ProfessionalRepository;


@Service
public class ProfessionalServiceImpl implements IProfessionalService {

    @Autowired
    private ProfessionalRepository repository;

    @Override
    public List<Professional> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Professional> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Professional create(Professional p) {
        return repository.save(p);
    }

    @Override
    public Professional update(Long id, Professional data) {

        Professional p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professional not found"));

        p.setFirstName(data.getFirstName());
        p.setLastName(data.getLastName());
        p.setEmail(data.getEmail());
        p.setPhone(data.getPhone());
        p.setCity(data.getCity());
        p.setAddress(data.getAddress());

        return repository.save(p);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}