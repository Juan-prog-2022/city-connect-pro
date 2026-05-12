package com.bluesoftware.city_connect_pro.dtos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class CreateAppointmentRequestDTO {

    @Schema(example = "1", description = "Client ID")
    @NotNull
    private Long clientId;

    @Schema(example = "2", description = "Professional ID")
    @NotNull
    private Long professionalId;

    @Schema(example = "2026-06-10T14:30:00", description = "Appointment date and time")
    @NotNull
    @Future
    private LocalDateTime appointmentDateTime;

    @Schema(example = "60", description = "Duration in minutes")
    @NotNull
    @Min(1)
    private Integer duration;

    @Schema(example = "ONLINE", description = "Appointment type")
    @NotNull
    private String type;

    @Schema(example = "Consultation about backend project")
    @Size(max = 255)
    private String reason;

    // getters & setters

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}