package com.bluesoftware.city_connect_pro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "mercadopago")
@Getter
@Setter
public class MercadoPagoProperties {

    /** Access Token de prueba o producción (TEST-... / APP_USR-...) */
    private String accessToken = "";

    private String notificationUrl = "http://localhost:8080/api/webhooks/mercadopago";

    private BackUrls backUrls = new BackUrls();

    @Getter
    @Setter
    public static class BackUrls {
        private String success = "http://localhost:5173/payment/success";
        private String failure = "http://localhost:5173/payment/failure";
        private String pending = "http://localhost:5173/payment/pending";
    }

    public boolean isConfigured() {
        return accessToken != null && !accessToken.isBlank();
    }
}
