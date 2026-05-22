package com.bluesoftware.city_connect_pro.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluesoftware.city_connect_pro.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByAppointmentIdOrderByCreatedAtDesc(Long appointmentId);

    Optional<Payment> findTopByAppointmentIdOrderByCreatedAtDesc(Long appointmentId);

    Optional<Payment> findByExternalPreferenceId(String externalPreferenceId);

    Optional<Payment> findByExternalPaymentId(String externalPaymentId);
}
