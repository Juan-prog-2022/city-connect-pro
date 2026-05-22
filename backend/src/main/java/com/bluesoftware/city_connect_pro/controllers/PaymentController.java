package com.bluesoftware.city_connect_pro.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluesoftware.city_connect_pro.dtos.PaymentResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Payment;
import com.bluesoftware.city_connect_pro.mapper.PaymentMapper;
import com.bluesoftware.city_connect_pro.services.IPaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Mercado Pago checkout and payment status")
public class PaymentController {

    private final IPaymentService paymentService;

    @Operation(summary = "Create Mercado Pago checkout for an appointment")
    @PostMapping("/appointments/{appointmentId}/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PAYMENT_CREATE')")
    public PaymentResponseDTO createCheckout(@PathVariable Long appointmentId) {
        Payment payment = paymentService.createMercadoPagoPayment(appointmentId);
        return PaymentMapper.toResponse(payment);
    }

    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_READ')")
    public PaymentResponseDTO getById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        return PaymentMapper.toResponse(payment);
    }

    @Operation(summary = "List payments for an appointment")
    @GetMapping("/appointments/{appointmentId}")
    @PreAuthorize("hasAuthority('PAYMENT_READ')")
    public List<PaymentResponseDTO> getByAppointment(@PathVariable Long appointmentId) {
        return paymentService.getPaymentsByAppointment(appointmentId)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }
}
