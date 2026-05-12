package com.bluesoftware.city_connect_pro.dtos;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private String username;
    private Long userId;
    private Set<String> roles;
    private String message;
}