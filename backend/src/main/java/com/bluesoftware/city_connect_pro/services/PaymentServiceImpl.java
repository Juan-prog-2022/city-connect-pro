package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluesoftware.city_connect_pro.entities.Appointment;
import com.bluesoftware.city_connect_pro.entities.Payment;
import com.bluesoftware.city_connect_pro.entities.PaymentProvider;
import com.bluesoftware.city_connect_pro.entities.PaymentStatus;
import com.bluesoftware.city_connect_pro.repositories.AppointmentRepository;
import com.bluesoftware.city_connect_pro.repositories.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final IMercadoPagoService mercadoPagoService;

    @Override
    @Transactional
    public Payment createMercadoPagoPayment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        if (appointment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Appointment is already paid");
        }

        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setProvider(PaymentProvider.MERCADO_PAGO);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setAmount(MercadoPagoServiceImpl.calculateAppointmentAmount(
                appointment.getProfessional().getHourlyRate(),
                appointment.getDuration()
        ));
        payment.setCurrencyId(
                appointment.getProfessional().getCurrency() != null
                        ? appointment.getProfessional().getCurrency().name()
                        : "ARS"
        );
        payment.setDescription(buildDescription(appointment));

        payment = paymentRepository.save(payment);

        String payerEmail = appointment.getClient().getEmail();
        return mercadoPagoService.createCheckoutPreference(payment, payerEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id).map(payment -> {
            payment.getAppointment().getId();
            return payment;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByAppointment(Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new EntityNotFoundException("Appointment not found");
        }
        return paymentRepository.findByAppointmentIdOrderByCreatedAtDesc(appointmentId);
    }

    @Override
    @Transactional
    public void processWebhook(String topic, String resourceId, String action, String dataId) {
        String paymentId = dataId != null ? dataId : resourceId;

        if (paymentId == null || paymentId.isBlank()) {
            return;
        }

        if ("payment".equalsIgnoreCase(topic)
                || (action != null && action.startsWith("payment."))
                || "payment".equalsIgnoreCase(action)) {
            mercadoPagoService.syncPaymentFromMercadoPago(paymentId);
        }
    }

    private String buildDescription(Appointment appointment) {
        return "Turno #%d - %s %s".formatted(
                appointment.getId(),
                appointment.getProfessional().getFirstName(),
                appointment.getProfessional().getLastName()
        );
    }
}
