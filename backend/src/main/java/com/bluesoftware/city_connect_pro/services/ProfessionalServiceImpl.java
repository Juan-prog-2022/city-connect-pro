package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.Professional;
import com.bluesoftware.city_connect_pro.repositories.ProfessionalRepository;
import com.bluesoftware.city_connect_pro.repositories.UserRepository;


@Service
public class ProfessionalServiceImpl implements IProfessionalService {

    @Autowired
    private ProfessionalRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Professional> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Professional> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Professional> getByUsername(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .flatMap(user -> repository.findByEmail(user.getEmail()));
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
        p.setLatitude(data.getLatitude());
        p.setLongitude(data.getLongitude());

        return repository.save(p);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Professional> findNearby(double latitude, double longitude, double radiusKm) {
        return repository.findNearby(latitude, longitude, radiusKm);
    }
}
