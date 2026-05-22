package com.bluesoftware.city_connect_pro.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    public String generateToken(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        Set<String> authorities = user.getRoles()
                .stream()
                .flatMap(role -> {
                    List<String> roleAuthorities = new ArrayList<>();
                    roleAuthorities.add(role.getName().name());
                    role.getPermissions().forEach(permission -> roleAuthorities.add(permission.getName().name()));
                    return roleAuthorities.stream();
                })
                .collect(Collectors.toSet());

        return generateToken(user.getUsername(), roles, authorities, Map.of("userId", user.getId()));
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Set<String> authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Set<String> roles = authorityNames.stream()
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        return generateToken(username, roles, authorityNames, Map.of());
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Collection<SimpleGrantedAuthority> extractAuthorities(String token) {
        List<?> authorities = parseClaims(token).get(JwtConstants.AUTHORITIES_CLAIM, List.class);

        if (authorities == null) {
            return List.of();
        }

        return authorities.stream()
                .map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private String generateToken(
            String username,
            Set<String> roles,
            Set<String> authorities,
            Map<String, Object> extraClaims
    ) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JwtConstants.EXPIRATION_MS);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .claim(JwtConstants.ROLES_CLAIM, roles)
                .claim(JwtConstants.AUTHORITIES_CLAIM, authorities)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(JwtConstants.SECRET_KEY)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(JwtConstants.SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
