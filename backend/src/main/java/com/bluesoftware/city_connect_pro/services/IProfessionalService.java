package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import com.bluesoftware.city_connect_pro.entities.Professional;

public interface IProfessionalService {

    List<Professional> getAll();

    Optional<Professional> getById(Long id);

    Optional<Professional> getByUsername(String username);

    Professional create(Professional professional);

    Professional update(Long id, Professional professional);

    void delete(Long id);

    List<Professional> findNearby(double latitude, double longitude, double radiusKm);
}
