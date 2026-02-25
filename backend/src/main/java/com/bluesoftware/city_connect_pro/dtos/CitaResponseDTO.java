package com.bluesoftware.city_connect_pro.dtos;

import java.time.LocalDateTime;

import com.bluesoftware.city_connect_pro.entities.EstadoCita;
import com.bluesoftware.city_connect_pro.entities.TipoCita;

import lombok.Data;

@Data
public class CitaResponseDTO {
    private Long id;
    private String nombreCliente;
    private String nombreProfesional;
    private String estado;
    private String tipoCita;
    private String motivo;
    private LocalDateTime fechaHora;
    private String linkReunion;
}
