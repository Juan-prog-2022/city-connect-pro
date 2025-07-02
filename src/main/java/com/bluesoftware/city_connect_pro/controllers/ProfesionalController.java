package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.entities.Especialidad;
import com.bluesoftware.city_connect_pro.entities.Profesional;
import com.bluesoftware.city_connect_pro.entities.Usuario;
import com.bluesoftware.city_connect_pro.services.ProfesionalService;
import com.bluesoftware.city_connect_pro.services.RoleService;
import com.bluesoftware.city_connect_pro.services.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/profesionales")
@CrossOrigin(origins = "*")
public class ProfesionalController {

    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    // 🔹 Obtener todos los profesionales - público
    @Operation(summary = "Listar todos los profesionales")
    @GetMapping
    public ResponseEntity<List<Profesional>> listarProfesionales() {
        return ResponseEntity.ok(profesionalService.listarProfesionales());
    }

    // 🔹 Obtener un profesional por ID - público
    @Operation(summary = "Obtener un profesional por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Profesional> obtenerPorId(@PathVariable Long id) {
        Optional<Profesional> profesional = profesionalService.buscarPorId(id);
        return profesional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Crear nuevo profesional (acceso autenticado o restringido)
    @Operation(summary = "Crear nuevo profesional")
    @PostMapping
    public ResponseEntity<?> crearProfesional(@Valid @RequestBody Profesional profesional, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        Profesional nuevo = profesionalService.guardarProfesional(profesional);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // 🔹 Actualizar un profesional - solo el propio profesional o admin
    @Operation(summary = "Actualizar un profesional")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProfesional(@PathVariable Long id,
                                                   @RequestBody Profesional profesional,
                                                   Authentication auth) {

        Optional<Profesional> profExistente = profesionalService.buscarPorId(id);
        if (profExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Obtener usuario autenticado
        Optional<Usuario> usuarioOpt = usuarioService.findByUsername(auth.getName());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        Usuario usuarioAuth = usuarioOpt.get();

        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean esMismoProfesional = profExistente.get().getId().equals(usuarioAuth.getId());

        if (!esAdmin && !esMismoProfesional) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para actualizar este profesional");
        }

        profesional.setId(id);
        Profesional actualizado = profesionalService.guardarProfesional(profesional);
        return ResponseEntity.ok(actualizado);
    }

    // 🔹 Eliminar un profesional - solo admin
    @Operation(summary = "Eliminar un profesional")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProfesional(@PathVariable Long id, Authentication auth) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!esAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo un administrador puede eliminar profesionales");
        }

        Optional<Profesional> profesional = profesionalService.buscarPorId(id);
        if (profesional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        profesionalService.eliminarProfesional(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Buscar profesionales por especialidad - público
    @Operation(summary = "Buscar profesionales por especialidad")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Profesional>> buscarPorEspecialidad(@PathVariable Especialidad especialidad) {
        return ResponseEntity.ok(profesionalService.buscarPorEspecialidad(especialidad));
    }

    // 🔹 Buscar profesionales por ciudad - público
    @Operation(summary = "Buscar profesionales por ciudad")
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Profesional>> buscarPorCiudad(@PathVariable String ciudad) {
        List<Profesional> profesionales = profesionalService.buscarPorCiudad(ciudad);
        return ResponseEntity.ok(profesionales);
    }


    // 🔹 Buscar profesionales por especialidad y ciudad - público
    @Operation(summary = "Buscar profesionales por especialidad y ciudad")
    @GetMapping("/buscar")
    public ResponseEntity<List<Profesional>> buscarPorEspecialidadYCiudad(
            @RequestParam Especialidad especialidad,
            @RequestParam String ciudad) {
        return ResponseEntity.ok(profesionalService.buscarPorEspecialidadYCiudad(especialidad, ciudad));
    }
}
