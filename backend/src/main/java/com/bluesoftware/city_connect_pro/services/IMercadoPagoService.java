package com.bluesoftware.city_connect_pro.services;

import java.math.BigDecimal;

import com.bluesoftware.city_connect_pro.entities.Payment;

public interface IMercadoPagoService {

    Payment createCheckoutPreference(Payment payment, String payerEmail);

    void syncPaymentFromMercadoPago(String mercadoPagoPaymentId);
}
