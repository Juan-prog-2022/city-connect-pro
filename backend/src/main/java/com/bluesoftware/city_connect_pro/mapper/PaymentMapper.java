package com.bluesoftware.city_connect_pro.mapper;

import com.bluesoftware.city_connect_pro.dtos.PaymentResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Payment;

public class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentResponseDTO toResponse(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .appointmentId(payment.getAppointment().getId())
                .provider(payment.getProvider())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .currencyId(payment.getCurrencyId())
                .description(payment.getDescription())
                .externalPreferenceId(payment.getExternalPreferenceId())
                .externalPaymentId(payment.getExternalPaymentId())
                .paymentUrl(payment.getPaymentUrl())
                .mpStatusDetail(payment.getMpStatusDetail())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
