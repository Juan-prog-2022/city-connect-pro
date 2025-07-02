package com.bluesoftware.city_connect_pro.services;

import com.bluesoftware.city_connect_pro.entities.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> buscarPorNombre(String nombre);
}
