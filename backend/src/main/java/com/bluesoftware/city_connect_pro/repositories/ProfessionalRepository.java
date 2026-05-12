package com.bluesoftware.city_connect_pro.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluesoftware.city_connect_pro.entities.Professional;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    List<Professional> findByCityIgnoreCase(String city);

    List<Professional> findBySpecialty(String specialty);

    List<Professional> findByActiveTrue();
}