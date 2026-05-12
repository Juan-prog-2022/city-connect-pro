package com.bluesoftware.city_connect_pro.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"client", "professional"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "appointments", indexes = {
        @Index(name = "idx_appointment_date", columnList = "appointmentDateTime"),
        @Index(name = "idx_appointment_client", columnList = "client_id"),
        @Index(name = "idx_appointment_professional", columnList = "professional_id"),
        @Index(name = "idx_appointment_status", columnList = "status")
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer duration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @Size(max = 255)
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    @Size(max = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Size(max = 500)
    private String meetingLink;

    @NotNull
    @Future(message = "Appointment date must be in the future")
    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    private LocalDateTime cancellationDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}