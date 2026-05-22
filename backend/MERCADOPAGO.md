# Mercado Pago + Ubicación

## Mercado Pago (backend)

1. Creá una aplicación en [Mercado Pago Developers](https://www.mercadopago.com.ar/developers/panel/app).
2. Copiá el **Access Token de prueba** (`TEST-...`).
3. Configurá la variable de entorno o `application.properties`:

```properties
mercadopago.access-token=TEST-tu-token-aqui
```

4. Reiniciá el backend.

### Flujo

1. Crear turno: `POST /api/appointments`
2. Generar checkout: `POST /api/payments/appointments/{appointmentId}/checkout`
3. Respuesta incluye `paymentUrl` → abrir en el navegador y pagar con tarjetas de prueba.
4. Mercado Pago notifica: `POST /api/webhooks/mercadopago` (en local usar [ngrok](https://ngrok.com): `ngrok http 8080` y actualizar `mercadopago.notification-url`).

### Tarjetas de prueba (Argentina)

Ver documentación oficial: [Tarjetas de prueba](https://www.mercadopago.com.ar/developers/es/docs/checkout-pro/additional-content/test-cards).

## Ubicación (Google Maps en el frontend)

- El **backend** guarda `latitude` y `longitude` en cada profesional.
- El **frontend** usa **Google Maps JavaScript API** (o React `@react-google-maps/api`) para:
  - Mostrar mapa y marcadores
  - Geocodificar dirección → lat/lng al registrar profesional
  - Buscar "cerca mío" con la ubicación del navegador

### API de proximidad (sin Google en el servidor)

```
GET /api/professionals/nearby?latitude=-22.5164&longitude=-63.8015&radiusKm=15
```

No hace falta librería de mapas en Java; Google Maps es gratis con cuota mensual (necesitás API Key en Google Cloud Console).
