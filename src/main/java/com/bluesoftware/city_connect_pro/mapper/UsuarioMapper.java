package com.bluesoftware.city_connect_pro.mapper;

import com.bluesoftware.city_connect_pro.dtos.UsuarioResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "roles", expression = "java(usuario.getRoles().stream().map(rol -> rol.getName()).collect(java.util.stream.Collectors.toSet()))")
    UsuarioResponseDTO usuarioToDto(Usuario usuario);
}
