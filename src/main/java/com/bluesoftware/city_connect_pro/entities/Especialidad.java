package com.bluesoftware.city_connect_pro.entities;

public enum Especialidad {
    ABOGADO("Abogado/a"),
    CONTADOR("Contador"),
    ESCRIBANO("Escribano"),
    ENFERMERO("Enfermero/a"),
    MAESTRO_PRIMARIA("Maestro Particular de Primaria"),
    MAESTRO_SECUNDARIA("Maestro Particular de Secundaria"),
    NUTRICION("Nutricionista"),
    PSICOLOGIA("Psicólogo/a"),
    PEDIATRIA("Pediatra");

    private final String descripcion;

    Especialidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}
