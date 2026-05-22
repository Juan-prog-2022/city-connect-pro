package com.bluesoftware.city_connect_pro.mapper;

import com.bluesoftware.city_connect_pro.dtos.ReviewResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Review;

public class ReviewMapper {

    private ReviewMapper() {
    }

    public static ReviewResponseDTO toResponse(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .clientId(review.getClient().getId())
                .clientName(review.getClient().getFirstName() + " " + review.getClient().getLastName())
                .professionalId(review.getProfessional().getId())
                .professionalName(
                        review.getProfessional().getFirstName() + " " + review.getProfessional().getLastName())
                .title(review.getTitle())
                .comment(review.getComment())
                .rating(review.getRating())
                .status(review.getStatus())
                .reviewDate(review.getReviewDate())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
