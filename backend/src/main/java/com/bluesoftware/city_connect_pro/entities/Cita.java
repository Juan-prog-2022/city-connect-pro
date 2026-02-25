package com.bluesoftware.city_connect_pro.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "citas")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer duracion;  // Duración de la cita en minutos

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado;  // Estado de la cita (pendiente, confirmada, etc.)

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;  // Relación con el usuario (cliente)

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;  // Relación con el profesional

    private String motivo;  // Motivo de la cita (opcional)

    @Enumerated(EnumType.STRING)
    private TipoCita tipoCita;  // Tipo de cita (online, presencial)

    private String comentarios;  // Comentarios adicionales (opcional)

    private String metodoPago;  // Método de pago (si aplica)

    private String linkReunion;  // Enlace de la reunión si es online (Zoom, Meet, etc.)

    @Column(nullable = false)
    private LocalDateTime fechaHora;  // Puedes usar LocalDateTime si prefieres trabajar con objetos de fecha/hora

    private LocalDateTime fechaCancelacion;  // Fecha de cancelación (si aplica)

}
