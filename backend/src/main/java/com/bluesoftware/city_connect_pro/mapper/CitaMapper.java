package com.bluesoftware.city_connect_pro.mapper;

import com.bluesoftware.city_connect_pro.dtos.CitaResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Cita;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitaMapper {

    @Mapping(target = "nombreCliente", expression = "java(cita.getUsuario().getNombre() + \" \" + cita.getUsuario().getApellido())")
    @Mapping(target = "nombreProfesional", expression = "java(cita.getProfesional().getNombre() + \" \" + cita.getProfesional().getApellido())")
    CitaResponseDTO citaToCitaResponseDTO(Cita cita);
}
