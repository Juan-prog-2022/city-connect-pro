package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.dtos.*;
import com.bluesoftware.city_connect_pro.mapper.*;
import com.bluesoftware.city_connect_pro.services.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    @Autowired
    private IProfessionalService service;

    @Operation(summary = "Get all professionals", description = "Retrieves a list of all professionals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professionals retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Professionals not found")
    })
    @GetMapping
    public List<ProfessionalResponseDTO> getAll() {
        return service.getAll()
                .stream()
                .map(ProfessionalMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Create professional", description = "Creates a new professional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Professional created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ProfessionalResponseDTO create(@RequestBody ProfessionalRequestDTO request) {
        return ProfessionalMapper.toResponse(
                service.create(ProfessionalMapper.toEntity(request))
        );
    }
}