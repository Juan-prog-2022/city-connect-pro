package com.bluesoftware.city_connect_pro.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluesoftware.city_connect_pro.entities.Review;
import com.bluesoftware.city_connect_pro.entities.ReviewStatus;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByClientIdAndProfessionalId(Long clientId, Long professionalId);

    Page<Review> findByProfessionalId(Long professionalId, Pageable pageable);

    Page<Review> findByProfessionalIdAndStatus(
            Long professionalId,
            ReviewStatus status,
            Pageable pageable
    );

    List<Review> findByProfessionalIdAndStatus(Long professionalId, ReviewStatus status);
}
