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
@Table(name = "resenias")
public class Resenia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String comentario;

    @Column(nullable = false)
    private Integer calificacion; // De 1 a 5

    @Column(nullable = false)
    private LocalDateTime fechaResenia;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    @Column(nullable = false)
    private Boolean visible = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoResenia estado = EstadoResenia.PENDIENTE;

}
