package com.bluesoftware.city_connect_pro.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bluesoftware.city_connect_pro.entities.*;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 🔍 Appointments by professional (paginado)
    Page<Appointment> findByProfessionalId(Long professionalId, Pageable pageable);

    // 🔍 Appointments by client (paginado)
    Page<Appointment> findByClientId(Long clientId, Pageable pageable);

    // 🔍 Filtrar por estado (cliente)
    Page<Appointment> findByClientIdAndStatus(
            Long clientId,
            AppointmentStatus status,
            Pageable pageable
    );

    // 🔍 Filtrar por estado (profesional)
    Page<Appointment> findByProfessionalIdAndStatus(
            Long professionalId,
            AppointmentStatus status,
            Pageable pageable
    );

    // 🔥 Buscar citas en un rango de fechas (agenda del profesional)
    List<Appointment> findByProfessionalIdAndAppointmentDateTimeBetween(
            Long professionalId,
            LocalDateTime start,
            LocalDateTime end
    );

    // 🔥 Próximas citas del cliente
    List<Appointment> findByClientIdAndAppointmentDateTimeAfter(
            Long clientId,
            LocalDateTime now
    );

    // 🔥 Validación simple (misma fecha exacta)
    boolean existsByProfessionalIdAndAppointmentDateTime(
            Long professionalId,
            LocalDateTime appointmentDateTime
    );

    // 🔥🔥 Validación REAL de solapamiento (PRO)
    @Query("""
        SELECT COUNT(a) > 0 FROM Appointment a
        WHERE a.professional.id = :professionalId
        AND a.status <> 'CANCELLED'
        AND (
            :start < a.appointmentDateTime + (a.duration * 1 minute)
            AND :end > a.appointmentDateTime
        )
    """)
    boolean existsOverlappingAppointment(
            Long professionalId,
            LocalDateTime start,
            LocalDateTime end
    );
}