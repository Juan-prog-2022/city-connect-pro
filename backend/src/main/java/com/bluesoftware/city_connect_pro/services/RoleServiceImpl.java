package com.bluesoftware.city_connect_pro.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bluesoftware.city_connect_pro.entities.Role;
import com.bluesoftware.city_connect_pro.entities.RoleName;
import com.bluesoftware.city_connect_pro.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
