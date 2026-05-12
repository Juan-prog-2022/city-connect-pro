package com.bluesoftware.city_connect_pro.mapper;

import com.bluesoftware.city_connect_pro.dtos.AppointmentResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Appointment;

public class AppointmentMapper {

    public static AppointmentResponseDTO toResponse(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .clientId(appointment.getClient().getId())
                .professionalId(appointment.getProfessional().getId())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .duration(appointment.getDuration())
                .status(appointment.getStatus())
                .paymentStatus(appointment.getPaymentStatus())
                .type(appointment.getType())
                .reason(appointment.getReason())
                .createdAt(appointment.getCreatedAt())
                .build();
    }
}