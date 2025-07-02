package com.bluesoftware.city_connect_pro.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfesionalResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String dni;
    private String email;
    private String habilidades;
    private String certificaciones;
    private String ciudad;
    private String direccion;
    private String horario;
    private double tarifaPorHora;
    private String moneda;
    private int anosExperiencia;
    private String descripcionExperiencia;
    private BigDecimal calificacionPromedio;
    private int numeroResenas;
    private Boolean activo;
    private Boolean verificado;
    private String fotoPerfil;
    private String videoPresentacion;
    private String especialidad;

}
