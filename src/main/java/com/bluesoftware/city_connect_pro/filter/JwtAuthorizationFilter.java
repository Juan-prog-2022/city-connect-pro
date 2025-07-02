package com.bluesoftware.city_connect_pro.filter;

import java.io.IOException;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.bluesoftware.city_connect_pro.security.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.bluesoftware.city_connect_pro.filter.TokenJwtConfig.*;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        // Si no tiene la cabecera Authorization, se sigue con la cadena de filtros o si no empieza con el prefijo Bearer
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        // Se obtiene el token y se valida
        String token = header.replace(PREFIX_TOKEN, "");

        try {
            // Se obtiene el usuario del token
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();

            // Se obtiene el username del token
            String username = claims.getSubject();

            // Se obtiene los roles del token
            Object authoritiesClaims = claims.get("authorities");

            @SuppressWarnings("unchecked")
            Collection<SimpleGrantedAuthority> authorities = ((List<Map<String, String>>) authoritiesClaims).stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .toList();


            // Se crea el objeto de autenticación
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Se autentica el usuario
            SecurityContextHolder.getContext().setAuthentication(authToken);
            chain.doFilter(request, response);

        } catch (JwtException e) {
            // Se crea la respuesta de error
            Map<String, String> responseError = new HashMap<>();
            responseError.put("error", e.getMessage());
            responseError.put("message", "Token no válido.");

            // Se escribe la respuesta
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseError));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }
    }
}