package com.bluesoftware.city_connect_pro.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.*;
import com.bluesoftware.city_connect_pro.repositories.AppointmentRepository;
import com.bluesoftware.city_connect_pro.repositories.ProfessionalRepository;
import com.bluesoftware.city_connect_pro.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    // 🔍 Obtener todos
    @Override
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    // 🔍 Obtener por ID
    @Override
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // 🔥 Crear turno con validaciones reales
    @Override
    @Transactional
    public Appointment createAppointment(
            Long clientId,
            Long professionalId,
            LocalDateTime dateTime,
            Integer duration,
            AppointmentType type,
            String reason
    ) {

        // 1️⃣ Validar cliente
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // 2️⃣ Validar profesional
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Professional not found"));

        // 3️⃣ Calcular fin del turno
        LocalDateTime end = dateTime.plusMinutes(duration);

        // 4️⃣ Validar solapamiento
        boolean existsConflict = appointmentRepository.existsOverlappingAppointment(
                professionalId,
                dateTime,
                end
        );

        if (existsConflict) {
            throw new IllegalStateException("Time slot is not available");
        }

        // 5️⃣ Crear appointment
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setProfessional(professional);
        appointment.setAppointmentDateTime(dateTime);
        appointment.setDuration(duration);
        appointment.setType(type);
        appointment.setReason(reason);

        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setPaymentStatus(PaymentStatus.PENDING);

        return appointmentRepository.save(appointment);
    }

    // 🔄 Update controlado
    @Override
    @Transactional
    public Appointment updateAppointment(Long id, Appointment updated) {

        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        // ⚠️ Solo permitir ciertos cambios
        existing.setReason(updated.getReason());
        existing.setNotes(updated.getNotes());
        existing.setPaymentMethod(updated.getPaymentMethod());

        return appointmentRepository.save(existing);
    }

    // ❌ Cancelar turno (soft logic)
    @Override
    @Transactional
    public void cancelAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        // Validación simple de estado
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationDate(LocalDateTime.now());

        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled appointments cannot be completed");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Appointment already completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    // 🗑️ Delete físico (opcional)
    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Appointment not found");
        }
        appointmentRepository.deleteById(id);
    }

    // 🔍 Por profesional
    @Override
    public Page<Appointment> getAppointmentsByProfessional(Long professionalId, Pageable pageable) {
        return appointmentRepository.findByProfessionalId(professionalId, pageable);
    }

    @Override
    public Page<Appointment> getAppointmentsByProfessionalUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Professional professional = professionalRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Professional profile not found"));

        return appointmentRepository.findByProfessionalId(professional.getId(), pageable);
    }

    // 🔍 Por cliente
    @Override
    public Page<Appointment> getAppointmentsByClient(Long clientId, Pageable pageable) {
        return appointmentRepository.findByClientId(clientId, pageable);
    }

    @Override
    public Page<Appointment> getAppointmentsByClientUsername(String username, Pageable pageable) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return appointmentRepository.findByClientId(user.getId(), pageable);
    }

    // 🔍 Por estado
    @Override
    public Page<Appointment> getAppointmentsByClientAndStatus(
            Long clientId,
            AppointmentStatus status,
            Pageable pageable
    ) {
        return appointmentRepository.findByClientIdAndStatus(clientId, status, pageable);
    }

    // 🔥 Validar disponibilidad
    @Override
    public boolean isTimeSlotAvailable(
            Long professionalId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        return !appointmentRepository.existsOverlappingAppointment(
                professionalId,
                start,
                end
        );
    }
}
