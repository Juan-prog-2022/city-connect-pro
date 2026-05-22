package com.bluesoftware.city_connect_pro.dtos;

import lombok.Data;

@Data
public class MercadoPagoWebhookDTO {

    private String action;
    private String type;
    private WebhookData data;

    @Data
    public static class WebhookData {
        private String id;
    }
}
