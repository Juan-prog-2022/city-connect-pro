package com.bluesoftware.city_connect_pro.dtos;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    private String dni;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;
}