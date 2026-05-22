package com.bluesoftware.city_connect_pro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bluesoftware.city_connect_pro.dtos.ProfessionalRequestDTO;
import com.bluesoftware.city_connect_pro.dtos.ProfessionalResponseDTO;
import com.bluesoftware.city_connect_pro.mapper.ProfessionalMapper;
import com.bluesoftware.city_connect_pro.services.IProfessionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    @Autowired
    private IProfessionalService service;

    @Operation(summary = "Get all professionals")
    @GetMapping
    @PreAuthorize("hasAuthority('PROFESSIONAL_READ') or hasRole('USER')")
    public List<ProfessionalResponseDTO> getAll() {
        return service.getAll()
                .stream()
                .map(ProfessionalMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get professionals near a location (Haversine, km)")
    @GetMapping("/nearby")
    @PreAuthorize("hasAuthority('PROFESSIONAL_READ') or hasRole('USER')")
    public List<ProfessionalResponseDTO> getNearby(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10") double radiusKm
    ) {
        return service.findNearby(latitude, longitude, radiusKm)
                .stream()
                .map(ProfessionalMapper::toResponse)
                .toList();
    }

    @Operation(summary = "Get current professional profile")
    @GetMapping("/me")
    @PreAuthorize("hasRole('PRO')")
    public ProfessionalResponseDTO getMe(Authentication authentication) {
        return service.getByUsername(authentication.getName())
                .map(ProfessionalMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Professional profile not found"));
    }

    @Operation(summary = "Get professional by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PROFESSIONAL_READ') or hasRole('USER')")
    public ProfessionalResponseDTO getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ProfessionalMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Professional not found"));
    }

    @Operation(summary = "Create professional")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PROFESSIONAL_CREATE')")
    public ProfessionalResponseDTO create(@Valid @RequestBody ProfessionalRequestDTO request) {
        return ProfessionalMapper.toResponse(
                service.create(ProfessionalMapper.toEntity(request))
        );
    }

    @Operation(summary = "Update professional")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PROFESSIONAL_UPDATE')")
    public ProfessionalResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody ProfessionalRequestDTO request
    ) {
        return ProfessionalMapper.toResponse(
                service.update(id, ProfessionalMapper.toEntity(request))
        );
    }

    @Operation(summary = "Delete professional")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('PROFESSIONAL_DELETE')")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Deleted"))
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
