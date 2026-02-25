package com.bluesoftware.city_connect_pro.mapper;

import com.bluesoftware.city_connect_pro.dtos.ProfesionalResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Profesional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfesionalMapper {

    @Mapping(target = "moneda", expression = "java(profesional.getMoneda() != null ? profesional.getMoneda().name() : null)")
    @Mapping(target = "especialidad", expression = "java(profesional.getEspecialidad() != null ? profesional.getEspecialidad().name() : null)")
    ProfesionalResponseDTO profesionalToProfesionalResponseDTO(Profesional profesional);
}
