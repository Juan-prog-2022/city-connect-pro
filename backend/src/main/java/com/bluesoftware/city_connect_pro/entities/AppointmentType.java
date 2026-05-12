package com.bluesoftware.city_connect_pro.entities;

public enum AppointmentType {

    ONLINE("Online"),
    IN_PERSON("In person");

    private final String label;

    AppointmentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}