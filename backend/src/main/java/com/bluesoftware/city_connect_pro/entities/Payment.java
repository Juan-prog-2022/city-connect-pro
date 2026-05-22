package com.bluesoftware.city_connect_pro.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "appointment")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "payments", indexes = {
        @Index(name = "idx_payment_appointment", columnList = "appointment_id"),
        @Index(name = "idx_payment_status", columnList = "status"),
        @Index(name = "idx_payment_preference", columnList = "external_preference_id"),
        @Index(name = "idx_payment_mp_id", columnList = "external_payment_id")
})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider = PaymentProvider.MERCADO_PAGO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currencyId = "ARS";

    @Column(length = 255)
    private String description;

    @Column(name = "external_preference_id", length = 100)
    private String externalPreferenceId;

    @Column(name = "external_payment_id", length = 100)
    private String externalPaymentId;

    @Column(name = "payment_url", length = 500)
    private String paymentUrl;

    @Column(name = "mp_status_detail", length = 100)
    private String mpStatusDetail;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
