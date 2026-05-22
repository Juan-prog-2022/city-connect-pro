package com.bluesoftware.city_connect_pro.dtos;

import java.time.LocalDateTime;

import com.bluesoftware.city_connect_pro.entities.ReviewStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDTO {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long professionalId;
    private String professionalName;

    private String title;
    private String comment;
    private Integer rating;
    private ReviewStatus status;

    private LocalDateTime reviewDate;
    private LocalDateTime updatedAt;
}
