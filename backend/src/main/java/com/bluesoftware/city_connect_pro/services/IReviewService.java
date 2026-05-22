package com.bluesoftware.city_connect_pro.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bluesoftware.city_connect_pro.entities.Review;
public interface IReviewService {

    Review createReview(
            Long clientId,
            Long professionalId,
            String title,
            String comment,
            Integer rating
    );

    Optional<Review> getReviewById(Long id);

    Page<Review> getAllReviews(Pageable pageable);

    Page<Review> getReviewsByProfessional(Long professionalId, Pageable pageable);

    Page<Review> getApprovedReviewsByProfessional(Long professionalId, Pageable pageable);

    Review approveReview(Long id);

    Review rejectReview(Long id);

    void deleteReview(Long id);
}
