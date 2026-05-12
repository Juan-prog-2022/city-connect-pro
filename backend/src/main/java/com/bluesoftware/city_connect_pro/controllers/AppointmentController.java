package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.dtos.*;
import com.bluesoftware.city_connect_pro.entities.*;
import com.bluesoftware.city_connect_pro.mapper.AppointmentMapper;
import com.bluesoftware.city_connect_pro.services.IAppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(
        name = "Appointments",
        description = "Manage appointments between clients and professionals"
)
public class AppointmentController {

    private final IAppointmentService appointmentService;

    // =====================================================
    // CREATE APPOINTMENT
    // =====================================================

    @Operation(
            summary = "Create appointment",
            description = "Creates a new appointment"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Appointment created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or unavailable slot"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client or professional not found"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('APPOINTMENT_CREATE')")
    public AppointmentResponseDTO create(
            @Valid @RequestBody CreateAppointmentRequestDTO request
    ) {

        Appointment appointment = appointmentService.createAppointment(
                request.getClientId(),
                request.getProfessionalId(),
                request.getAppointmentDateTime(),
                request.getDuration(),
                AppointmentType.valueOf(
                        request.getType().toUpperCase()
                ),
                request.getReason()
        );

        return AppointmentMapper.toResponse(appointment);
    }

    // =====================================================
    // GET ALL APPOINTMENTS
    // =====================================================

    @Operation(
            summary = "Get all appointments",
            description = "Returns paginated appointments"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public Page<AppointmentResponseDTO> getAll(

            @PageableDefault(
                    size = 10,
                    sort = "appointmentDateTime"
            )
            Pageable pageable
    ) {

        return appointmentService.getAllAppointments(pageable)
                .map(AppointmentMapper::toResponse);
    }

    // =====================================================
    // GET APPOINTMENT BY ID
    // =====================================================

    @Operation(summary = "Get appointment by ID")
    @ApiResponse(
            responseCode = "404",
            description = "Appointment not found"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public AppointmentResponseDTO getById(
            @PathVariable Long id
    ) {

        Appointment appointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found")
                );

        return AppointmentMapper.toResponse(appointment);
    }

    // =====================================================
    // GET APPOINTMENTS BY PROFESSIONAL
    // =====================================================

    @Operation(summary = "Get appointments by professional")
    @GetMapping("/professional/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public Page<AppointmentResponseDTO> getByProfessional(

            @PathVariable Long id,

            @PageableDefault(
                    size = 10,
                    sort = "appointmentDateTime"
            )
            Pageable pageable
    ) {

        return appointmentService.getAppointmentsByProfessional(id, pageable)
                .map(AppointmentMapper::toResponse);
    }

    // =====================================================
    // GET APPOINTMENTS BY CLIENT
    // =====================================================

    @Operation(summary = "Get appointments by client")
    @GetMapping("/client/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public Page<AppointmentResponseDTO> getByClient(

            @PathVariable Long id,

            @PageableDefault(
                    size = 10,
                    sort = "appointmentDateTime"
            )
            Pageable pageable
    ) {

        return appointmentService.getAppointmentsByClient(id, pageable)
                .map(AppointmentMapper::toResponse);
    }

    // =====================================================
    // CANCEL APPOINTMENT
    // =====================================================

    @Operation(summary = "Cancel appointment")
    @ApiResponse(
            responseCode = "400",
            description = "Appointment already cancelled"
    )
    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE')")
    public void cancel(
            @PathVariable Long id
    ) {

        appointmentService.cancelAppointment(id);
    }
}