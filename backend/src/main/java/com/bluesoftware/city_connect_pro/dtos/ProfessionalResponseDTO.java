package com.bluesoftware.city_connect_pro.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProfessionalResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private List<String> skills;
    private List<String> certifications;

    private String city;
    private String address;
    private Double latitude;
    private Double longitude;

    private BigDecimal hourlyRate;
    private String currency;

    private int yearsOfExperience;
    private BigDecimal averageRating;
    private int reviewCount;

    private String specialty;
}