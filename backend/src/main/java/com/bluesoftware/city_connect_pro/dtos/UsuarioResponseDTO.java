package com.bluesoftware.city_connect_pro.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String telefono;
    private String direccion;
    private boolean activo;
    private Set<String> roles;  // Solo los nombres de los roles
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean admin;

}
