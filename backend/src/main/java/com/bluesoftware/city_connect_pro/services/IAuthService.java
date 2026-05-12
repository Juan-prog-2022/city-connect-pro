package com.bluesoftware.city_connect_pro.services;

import com.bluesoftware.city_connect_pro.dtos.AuthResponse;
import com.bluesoftware.city_connect_pro.dtos.LoginRequest;
import com.bluesoftware.city_connect_pro.dtos.RegisterRequest;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}