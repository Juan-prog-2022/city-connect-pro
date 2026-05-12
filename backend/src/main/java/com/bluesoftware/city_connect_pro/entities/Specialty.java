package com.bluesoftware.city_connect_pro.entities;

public enum Specialty {

    LAWYER("Lawyer"),
    ACCOUNTANT("Accountant"),
    NOTARY("Notary"),
    NURSE("Nurse"),
    PRIMARY_TUTOR("Primary School Tutor"),
    SECONDARY_TUTOR("Secondary School Tutor"),
    NUTRITIONIST("Nutritionist"),
    PSYCHOLOGIST("Psychologist"),
    PEDIATRICIAN("Pediatrician");

    private final String description;

    Specialty(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}