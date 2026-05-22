package com.bluesoftware.city_connect_pro.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bluesoftware.city_connect_pro.entities.*;

public interface IAppointmentService {

    // 🔍 Obtener todos (paginado)
    Page<Appointment> getAllAppointments(Pageable pageable);

    // 🔍 Obtener por ID
    Optional<Appointment> getAppointmentById(Long id);

    // 🔥 Crear turno (con validaciones de negocio)
    Appointment createAppointment(
            Long clientId,
            Long professionalId,
            LocalDateTime dateTime,
            Integer duration,
            AppointmentType type,
            String reason
    );

    // 🔄 Actualizar turno (controlado)
    Appointment updateAppointment(Long id, Appointment appointment);

    // ❌ Cancelar turno (no borrar físicamente)
    void cancelAppointment(Long id);

    // ✅ Marcar turno como completado
    Appointment completeAppointment(Long id);

    // 🗑️ (opcional) eliminar físico
    void deleteAppointment(Long id);

    // 🔍 Por profesional (paginado)
    Page<Appointment> getAppointmentsByProfessional(Long professionalId, Pageable pageable);

    // 🔍 Por usuario profesional autenticado
    Page<Appointment> getAppointmentsByProfessionalUsername(String username, Pageable pageable);

    // 🔍 Por cliente (paginado)
    Page<Appointment> getAppointmentsByClient(Long clientId, Pageable pageable);

    // 🔍 Por cliente autenticado
    Page<Appointment> getAppointmentsByClientUsername(String username, Pageable pageable);

    // 🔍 Filtrar por estado
    Page<Appointment> getAppointmentsByClientAndStatus(
            Long clientId,
            AppointmentStatus status,
            Pageable pageable
    );

    // 🔥 Disponibilidad (clave para frontend)
    boolean isTimeSlotAvailable(
            Long professionalId,
            LocalDateTime start,
            LocalDateTime end
    );
}
