package com.bluesoftware.city_connect_pro.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluesoftware.city_connect_pro.entities.Role;
import com.bluesoftware.city_connect_pro.entities.RoleName;
import com.bluesoftware.city_connect_pro.services.IRoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleService service;

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getByName(@PathVariable RoleName name) {
        return service.findByName(name)
                .map(this::toMap)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Map<String, Object> toMap(Role role) {
        return Map.of(
                "id", role.getId(),
                "name", role.getName()
        );
    }
}
