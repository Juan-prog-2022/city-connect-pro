package com.bluesoftware.city_connect_pro.services;

import com.bluesoftware.city_connect_pro.entities.Role;
import com.bluesoftware.city_connect_pro.entities.RoleName;

import java.util.Optional;

public interface RoleService {
    Optional<Role> buscarPorNombre(RoleName nombre);
}
