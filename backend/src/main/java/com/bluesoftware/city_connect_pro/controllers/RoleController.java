package com.bluesoftware.city_connect_pro.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bluesoftware.city_connect_pro.services.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleService service;

    @GetMapping
    public List<RoleResponseDTO> getAll() {

        return service.getAll()
                .stream()
                .map(RoleMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public RoleResponseDTO getById(@PathVariable Long id) {

        return RoleMapper.toResponse(
                service.getById(id)
        );
    }

    @PostMapping
    public RoleResponseDTO create(
            @Valid @RequestBody RoleRequestDTO request
    ) {

        return RoleMapper.toResponse(
                service.create(
                        RoleMapper.toEntity(request)
                )
        );
    }

    @PutMapping("/{id}")
    public RoleResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO request
    ) {

        return RoleMapper.toResponse(
                service.update(
                        id,
                        RoleMapper.toEntity(request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}