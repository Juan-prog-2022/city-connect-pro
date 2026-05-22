package com.bluesoftware.city_connect_pro.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public final class JwtConstants {

    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final long EXPIRATION_MS = 86_400_000L;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN_URL = "/api/auth/login";
    public static final String CONTENT_TYPE = "application/json";
    public static final String ROLES_CLAIM = "roles";
    public static final String AUTHORITIES_CLAIM = "authorities";

    private JwtConstants() {
    }
}
