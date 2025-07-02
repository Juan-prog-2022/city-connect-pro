package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.dtos.CitaResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Cita;
import com.bluesoftware.city_connect_pro.entities.Profesional;
import com.bluesoftware.city_connect_pro.entities.Usuario;
import com.bluesoftware.city_connect_pro.mapper.CitaMapper;
import com.bluesoftware.city_connect_pro.services.CitaService;
import com.bluesoftware.city_connect_pro.services.ProfesionalService;
import com.bluesoftware.city_connect_pro.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private CitaMapper citaMapper;

    // 🔹 Obtener todas las citas (solo para admin)
    @Operation(summary = "Listar todas las citas (ADMIN)")
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> listarCitas() {
        List<Cita> citas = citaService.listarCitas();

        // Mapear lista de entidades a lista de DTOs
        List<CitaResponseDTO> dtos = citas.stream()
                .map(citaMapper::citaToCitaResponseDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // 🔹 Listar citas del usuario autenticado (ya sea cliente o profesional)
    @Operation(summary = "Listar mis citas según rol")
    @GetMapping("/mis-citas")
    public ResponseEntity<?> obtenerMisCitas(Authentication auth) {
        Usuario usuario = obtenerUsuarioAutenticado(auth);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        List<Cita> citas;
        if (tieneRolAdmin(auth)) {
            citas = citaService.listarCitas();
        } else if (tieneRolProfesional(auth)) {
            citas = citaService.buscarPorProfesional(usuario.getId());
        } else {
            citas = citaService.buscarPorUsuario(usuario.getId());
        }
        return ResponseEntity.ok(citas);
    }

    @PostMapping
    public ResponseEntity<?> crearCita(@Valid @RequestBody Cita cita, Authentication auth) {
        Usuario usuario = obtenerUsuarioAutenticado(auth);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        cita.setUsuario(usuario);

        if (cita.getProfesional() == null || cita.getProfesional().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe asignar un profesional válido a la cita.");
        }

        Cita nuevaCita = citaService.guardarCita(cita);
        CitaResponseDTO dto = citaMapper.citaToCitaResponseDTO(nuevaCita);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    // 🔹 Eliminar una cita (solo admin)
    @Operation(summary = "Eliminar cita por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCita(@PathVariable Long id, Authentication auth) {
        if (!tieneRolAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo un administrador puede eliminar citas.");
        }

        Optional<Cita> cita = citaService.buscarCitaPorId(id);
        if (cita.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cita no encontrada.");
        }

        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    // === Métodos privados para reutilizar lógica ===

    private Usuario obtenerUsuarioAutenticado(Authentication auth) {
        String username = auth.getName();
        return usuarioService.findByUsername(username).orElse(null);
    }

    private boolean tieneRolAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean tieneRolProfesional(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESIONAL"));
    }
}
