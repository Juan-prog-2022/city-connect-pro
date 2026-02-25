package com.bluesoftware.city_connect_pro.entities;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profesionales")
public class Profesional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String apellido;

    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String telefono;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El DNI no puede estar vacío")
    private String dni;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un email válido")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String habilidades;

    @Column(columnDefinition = "TEXT")
    private String certificaciones;

    private String ciudad;

    private String direccion;

    private String horario;

    @Min(value = 0, message = "La tarifa por hora no puede ser negativa")
    private double tarifaPorHora;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @Min(value = 0, message = "Los anios de experiencia no pueden ser negativos")
    @Max(value = 50, message = "Los anios de experiencia no pueden exceder 50")
    private int anosExperiencia;

    @Column(columnDefinition = "TEXT")
    private String descripcionExperiencia;

    private BigDecimal calificacionPromedio;

    private int numeroResenas;

    private Boolean activo = true;

    private Boolean verificado = false;

    @Size(max = 255, message = "La URL de la foto no puede exceder 255 caracteres")
    private String fotoPerfil;

    private String videoPresentacion;

    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaResenia DESC")
    private List<Resenia> resenias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especialidad especialidad;

}