package com.bluesoftware.city_connect_pro.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class CreateReviewRequestDTO {

    @Schema(example = "1", description = "Client user ID")
    @NotNull
    private Long clientId;

    @Schema(example = "2", description = "Professional ID")
    @NotNull
    private Long professionalId;

    @Schema(example = "Excellent service")
    @NotBlank
    @Size(max = 100)
    private String title;

    @Schema(example = "Very professional and helpful throughout the consultation.")
    @NotBlank
    @Size(max = 1000)
    private String comment;

    @Schema(example = "5", description = "Rating from 1 to 5")
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
