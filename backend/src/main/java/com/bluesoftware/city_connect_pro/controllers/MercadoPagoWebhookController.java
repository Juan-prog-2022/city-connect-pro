package com.bluesoftware.city_connect_pro.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluesoftware.city_connect_pro.dtos.MercadoPagoWebhookDTO;
import com.bluesoftware.city_connect_pro.services.IPaymentService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/webhooks/mercadopago")
@RequiredArgsConstructor
@Hidden
public class MercadoPagoWebhookController {

    private final IPaymentService paymentService;

    /**
     * Webhook IPN de Mercado Pago (JSON).
     * En local usar ngrok: ngrok http 8080 y configurar notification-url.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void handleWebhook(
            @RequestBody(required = false) MercadoPagoWebhookDTO body,
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "id", required = false) String queryId
    ) {
        String dataId = body != null && body.getData() != null ? body.getData().getId() : null;
        String action = body != null ? body.getAction() : null;
        String type = body != null ? body.getType() : topic;

        paymentService.processWebhook(type, queryId, action, dataId);
    }
}
