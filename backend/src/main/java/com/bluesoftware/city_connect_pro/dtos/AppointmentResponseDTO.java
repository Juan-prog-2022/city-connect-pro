package com.bluesoftware.city_connect_pro.dtos;

import java.time.LocalDateTime;

import com.bluesoftware.city_connect_pro.entities.*;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentResponseDTO {

    private Long id;
    private Long clientId;
    private Long professionalId;

    private LocalDateTime appointmentDateTime;
    private Integer duration;

    private AppointmentStatus status;
    private PaymentStatus paymentStatus;

    private AppointmentType type;
    private String reason;

    private LocalDateTime createdAt;
}