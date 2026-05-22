package com.bluesoftware.city_connect_pro.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bluesoftware.city_connect_pro.entities.Professional;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    List<Professional> findByCityIgnoreCase(String city);

    List<Professional> findBySpecialty(String specialty);

    List<Professional> findByActiveTrue();

    Optional<Professional> findByEmail(String email);

    @Query(value = """
            SELECT * FROM professionals p
            WHERE p.active = true
              AND p.latitude IS NOT NULL
              AND p.longitude IS NOT NULL
              AND (
                6371 * acos(
                  cos(radians(:lat)) * cos(radians(p.latitude))
                  * cos(radians(p.longitude) - radians(:lng))
                  + sin(radians(:lat)) * sin(radians(p.latitude))
                )
              ) <= :radiusKm
            ORDER BY (
                6371 * acos(
                  cos(radians(:lat)) * cos(radians(p.latitude))
                  * cos(radians(p.longitude) - radians(:lng))
                  + sin(radians(:lat)) * sin(radians(p.latitude))
                )
            )
            """, nativeQuery = true)
    List<Professional> findNearby(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radiusKm") double radiusKm
    );
}
