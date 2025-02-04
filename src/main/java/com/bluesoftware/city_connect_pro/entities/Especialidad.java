package com.bluesoftware.city_connect_pro.entities;

public enum Especialidad {
    ABOGADO("Abogado"),
    CONTADOR("Contador"),
    ESCRIBANO("Escribano"),
    ENFERMERO("Enfermero/a"),
    MAESTRO_PRIMARIA("Maestro Particular de Primaria"),
    MAESTRO_SECUNDARIA("Maestro Particular de Secundaria");

    private final String descripcion;

    Especialidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
