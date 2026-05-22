package com.bluesoftware.city_connect_pro.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.Professional;
import com.bluesoftware.city_connect_pro.entities.Review;
import com.bluesoftware.city_connect_pro.entities.ReviewStatus;
import com.bluesoftware.city_connect_pro.entities.User;
import com.bluesoftware.city_connect_pro.repositories.ProfessionalRepository;
import com.bluesoftware.city_connect_pro.repositories.ReviewRepository;
import com.bluesoftware.city_connect_pro.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;

    @Override
    @Transactional
    public Review createReview(
            Long clientId,
            Long professionalId,
            String title,
            String comment,
            Integer rating
    ) {
        if (reviewRepository.findByClientIdAndProfessionalId(clientId, professionalId).isPresent()) {
            throw new IllegalStateException("Client already reviewed this professional");
        }

        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Professional not found"));

        Review review = new Review();
        review.setClient(client);
        review.setProfessional(professional);
        review.setTitle(title);
        review.setComment(comment);
        review.setRating(rating);
        review.setStatus(ReviewStatus.PENDING);

        return reviewRepository.save(review);
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Page<Review> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Page<Review> getReviewsByProfessional(Long professionalId, Pageable pageable) {
        if (!professionalRepository.existsById(professionalId)) {
            throw new EntityNotFoundException("Professional not found");
        }
        return reviewRepository.findByProfessionalId(professionalId, pageable);
    }

    @Override
    public Page<Review> getApprovedReviewsByProfessional(Long professionalId, Pageable pageable) {
        if (!professionalRepository.existsById(professionalId)) {
            throw new EntityNotFoundException("Professional not found");
        }
        return reviewRepository.findByProfessionalIdAndStatus(
                professionalId,
                ReviewStatus.APPROVED,
                pageable
        );
    }

    @Override
    @Transactional
    public Review approveReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (review.getStatus() == ReviewStatus.APPROVED) {
            throw new IllegalStateException("Review is already approved");
        }

        review.setStatus(ReviewStatus.APPROVED);
        Review saved = reviewRepository.save(review);

        recalculateProfessionalRating(saved.getProfessional().getId());

        return saved;
    }

    @Override
    @Transactional
    public Review rejectReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (review.getStatus() == ReviewStatus.REJECTED) {
            throw new IllegalStateException("Review is already rejected");
        }

        boolean wasApproved = review.getStatus() == ReviewStatus.APPROVED;
        review.setStatus(ReviewStatus.REJECTED);
        Review saved = reviewRepository.save(review);

        if (wasApproved) {
            recalculateProfessionalRating(saved.getProfessional().getId());
        }

        return saved;
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        Long professionalId = review.getProfessional().getId();
        boolean wasApproved = review.getStatus() == ReviewStatus.APPROVED;

        reviewRepository.delete(review);

        if (wasApproved) {
            recalculateProfessionalRating(professionalId);
        }
    }

    private void recalculateProfessionalRating(Long professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Professional not found"));

        List<Review> approvedReviews = reviewRepository.findByProfessionalIdAndStatus(
                professionalId,
                ReviewStatus.APPROVED
        );

        int count = approvedReviews.size();
        professional.setReviewCount(count);

        if (count == 0) {
            professional.setAverageRating(BigDecimal.ZERO);
        } else {
            double average = approvedReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            professional.setAverageRating(
                    BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP)
            );
        }

        professionalRepository.save(professional);
    }
}
