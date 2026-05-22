package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import com.bluesoftware.city_connect_pro.entities.Payment;

public interface IPaymentService {

    Payment createMercadoPagoPayment(Long appointmentId);

    Optional<Payment> getPaymentById(Long id);

    List<Payment> getPaymentsByAppointment(Long appointmentId);

    void processWebhook(String topic, String resourceId, String action, String dataId);
}
