package com.bluesoftware.city_connect_pro.entities;

public enum PaymentStatus {

    PENDING("Pending"),
    PAID("Paid"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}