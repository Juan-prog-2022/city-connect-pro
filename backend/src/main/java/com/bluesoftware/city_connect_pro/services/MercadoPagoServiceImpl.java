package com.bluesoftware.city_connect_pro.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluesoftware.city_connect_pro.config.MercadoPagoProperties;
import com.bluesoftware.city_connect_pro.entities.Appointment;
import com.bluesoftware.city_connect_pro.entities.Payment;
import com.bluesoftware.city_connect_pro.entities.PaymentMethod;
import com.bluesoftware.city_connect_pro.entities.PaymentStatus;
import com.bluesoftware.city_connect_pro.repositories.AppointmentRepository;
import com.bluesoftware.city_connect_pro.repositories.PaymentRepository;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MercadoPagoServiceImpl implements IMercadoPagoService {

    private final MercadoPagoProperties properties;
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public Payment createCheckoutPreference(Payment payment, String payerEmail) {
        ensureConfigured();

        Appointment appointment = payment.getAppointment();

        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .id("appointment-" + appointment.getId())
                .title(payment.getDescription())
                .quantity(1)
                .currencyId(payment.getCurrencyId())
                .unitPrice(payment.getAmount())
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(List.of(item))
                .externalReference(appointment.getId().toString())
                .notificationUrl(properties.getNotificationUrl())
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success(properties.getBackUrls().getSuccess())
                        .failure(properties.getBackUrls().getFailure())
                        .pending(properties.getBackUrls().getPending())
                        .build())
                .autoReturn("approved")
                .build();

        try {
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            payment.setExternalPreferenceId(preference.getId());
            payment.setPaymentUrl(resolveInitPoint(preference));
            payment.setStatus(PaymentStatus.PENDING);

            appointment.setPaymentMethod(PaymentMethod.MERCADO_PAGO);
            appointment.setPaymentStatus(PaymentStatus.PENDING);
            appointmentRepository.save(appointment);

            return paymentRepository.save(payment);
        } catch (MPApiException | MPException e) {
            throw new IllegalStateException("Error creating Mercado Pago preference: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void syncPaymentFromMercadoPago(String mercadoPagoPaymentId) {
        ensureConfigured();

        try {
            PaymentClient client = new PaymentClient();
            com.mercadopago.resources.payment.Payment mpPayment =
                    client.get(Long.parseLong(mercadoPagoPaymentId));

            Payment payment = paymentRepository.findByExternalPaymentId(mercadoPagoPaymentId)
                    .or(() -> resolveByExternalReference(mpPayment.getExternalReference()))
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Payment not found for Mercado Pago id: " + mercadoPagoPaymentId));

            payment.setExternalPaymentId(mercadoPagoPaymentId);
            payment.setMpStatusDetail(mpPayment.getStatusDetail());
            payment.setStatus(mapStatus(mpPayment.getStatus()));

            Appointment appointment = payment.getAppointment();
            appointment.setPaymentMethod(PaymentMethod.MERCADO_PAGO);
            appointment.setPaymentStatus(payment.getStatus());

            paymentRepository.save(payment);
            appointmentRepository.save(appointment);
        } catch (MPApiException | MPException e) {
            throw new IllegalStateException("Error fetching Mercado Pago payment: " + e.getMessage(), e);
        }
    }

    public static BigDecimal calculateAppointmentAmount(BigDecimal hourlyRate, int durationMinutes) {
        return hourlyRate
                .multiply(BigDecimal.valueOf(durationMinutes))
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }

    private java.util.Optional<Payment> resolveByExternalReference(String externalReference) {
        if (externalReference == null || externalReference.isBlank()) {
            return java.util.Optional.empty();
        }
        try {
            Long appointmentId = Long.parseLong(externalReference);
            return paymentRepository.findTopByAppointmentIdOrderByCreatedAtDesc(appointmentId);
        } catch (NumberFormatException e) {
            return java.util.Optional.empty();
        }
    }

    private String resolveInitPoint(Preference preference) {
        if (preference.getInitPoint() != null) {
            return preference.getInitPoint();
        }
        return preference.getSandboxInitPoint();
    }

    private PaymentStatus mapStatus(String mpStatus) {
        if (mpStatus == null) {
            return PaymentStatus.PENDING;
        }
        return switch (mpStatus.toLowerCase()) {
            case "approved" -> PaymentStatus.PAID;
            case "rejected", "cancelled" -> PaymentStatus.FAILED;
            case "refunded", "charged_back" -> PaymentStatus.REFUNDED;
            default -> PaymentStatus.PENDING;
        };
    }

    private void ensureConfigured() {
        if (!properties.isConfigured()) {
            throw new IllegalStateException(
                    "Mercado Pago is not configured. Set mercadopago.access-token in application.properties");
        }
        com.mercadopago.MercadoPagoConfig.setAccessToken(properties.getAccessToken());
    }
}
