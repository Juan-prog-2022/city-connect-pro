package com.bluesoftware.city_connect_pro.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProfessionalRequestDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String dni;

    @Email
    @NotBlank
    private String email;

    private String phone;

    private List<String> skills;
    private List<String> certifications;

    private String city;
    private String address;

    @DecimalMin("0.0")
    private BigDecimal hourlyRate;

    private String currency;

    private int yearsOfExperience;

    private String specialty;
}