package com.bluesoftware.city_connect_pro.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MercadoPagoSdkInitializer {

    private final MercadoPagoProperties properties;

    @PostConstruct
    void initSdk() {
        if (properties.isConfigured()) {
            com.mercadopago.MercadoPagoConfig.setAccessToken(properties.getAccessToken());
        }
    }
}
