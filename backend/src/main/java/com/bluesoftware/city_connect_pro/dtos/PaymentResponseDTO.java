package com.bluesoftware.city_connect_pro.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bluesoftware.city_connect_pro.entities.PaymentProvider;
import com.bluesoftware.city_connect_pro.entities.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Long appointmentId;
    private PaymentProvider provider;
    private PaymentStatus status;
    private BigDecimal amount;
    private String currencyId;
    private String description;
    private String externalPreferenceId;
    private String externalPaymentId;
    private String paymentUrl;
    private String mpStatusDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
