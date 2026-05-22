package com.bluesoftware.city_connect_pro.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluesoftware.city_connect_pro.dtos.CreateReviewRequestDTO;
import com.bluesoftware.city_connect_pro.dtos.ReviewResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Review;
import com.bluesoftware.city_connect_pro.mapper.ReviewMapper;
import com.bluesoftware.city_connect_pro.services.IReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Manage client reviews for professionals")
public class ReviewController {

    private final IReviewService reviewService;

    @Operation(summary = "Get all reviews")
    @GetMapping
    @PreAuthorize("hasAuthority('REVIEW_READ')")
    public Page<ReviewResponseDTO> getAll(
            @PageableDefault(size = 10, sort = "reviewDate") Pageable pageable
    ) {
        return reviewService.getAllReviews(pageable)
                .map(ReviewMapper::toResponse);
    }

    @Operation(summary = "Create review", description = "Creates a new review pending approval")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate review"),
            @ApiResponse(responseCode = "404", description = "Client or professional not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('REVIEW_CREATE')")
    public ReviewResponseDTO create(@Valid @RequestBody CreateReviewRequestDTO request) {
        Review review = reviewService.createReview(
                request.getClientId(),
                request.getProfessionalId(),
                request.getTitle(),
                request.getComment(),
                request.getRating()
        );
        return ReviewMapper.toResponse(review);
    }

    @Operation(summary = "Get review by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('REVIEW_READ')")
    public ReviewResponseDTO getById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        return ReviewMapper.toResponse(review);
    }

    @Operation(summary = "Get reviews by professional", description = "Returns all reviews for a professional")
    @GetMapping("/professional/{professionalId}")
    @PreAuthorize("hasAuthority('REVIEW_READ')")
    public Page<ReviewResponseDTO> getByProfessional(
            @PathVariable Long professionalId,
            @PageableDefault(size = 10, sort = "reviewDate") Pageable pageable
    ) {
        return reviewService.getReviewsByProfessional(professionalId, pageable)
                .map(ReviewMapper::toResponse);
    }

    @Operation(summary = "Get approved reviews by professional")
    @GetMapping("/professional/{professionalId}/approved")
    @PreAuthorize("hasAuthority('REVIEW_READ')")
    public Page<ReviewResponseDTO> getApprovedByProfessional(
            @PathVariable Long professionalId,
            @PageableDefault(size = 10, sort = "reviewDate") Pageable pageable
    ) {
        return reviewService.getApprovedReviewsByProfessional(professionalId, pageable)
                .map(ReviewMapper::toResponse);
    }

    @Operation(summary = "Approve review", description = "Approves a review and updates professional rating")
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('REVIEW_UPDATE')")
    public ReviewResponseDTO approve(@PathVariable Long id) {
        return ReviewMapper.toResponse(reviewService.approveReview(id));
    }

    @Operation(summary = "Reject review")
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('REVIEW_UPDATE')")
    public ReviewResponseDTO reject(@PathVariable Long id) {
        return ReviewMapper.toResponse(reviewService.rejectReview(id));
    }

    @Operation(summary = "Delete review")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('REVIEW_DELETE')")
    public void delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}
