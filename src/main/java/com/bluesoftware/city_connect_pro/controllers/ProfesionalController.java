package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.entities.Especialidad;
import com.bluesoftware.city_connect_pro.entities.Profesional;
import com.bluesoftware.city_connect_pro.services.ProfesionalService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profesionales")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen
public class ProfesionalController {

    @Autowired
    private ProfesionalService profesionalService;

    // 🔹 Obtener todos los profesionales
    @Operation(summary = "Listar todos los profesionales")
    @GetMapping
    public ResponseEntity<List<Profesional>> listarProfesionales() {
        return ResponseEntity.ok(profesionalService.listarProfesionales());
    }

    // 🔹 Obtener un profesional por ID
    @Operation(summary = "Obtener un profesional por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Profesional> obtenerPorId(@PathVariable Long id) {
        Optional<Profesional> profesional = profesionalService.buscarPorId(id);
        return profesional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Crear un nuevo profesional
    @Operation(summary = "Crear un nuevo profesional")
    @PostMapping
    public ResponseEntity<Profesional> crearProfesional(@RequestBody Profesional profesional) {
        Profesional nuevoProfesional = profesionalService.guardarProfesional(profesional);
        return ResponseEntity.ok(nuevoProfesional);
    }

    // 🔹 Actualizar un profesional
    @Operation(summary = "Actualizar un profesional")
    @PutMapping("/{id}")
    public ResponseEntity<Profesional> actualizarProfesional(@PathVariable Long id, @RequestBody Profesional profesional) {
        if (!profesionalService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        profesional.setId(id);
        return ResponseEntity.ok(profesionalService.guardarProfesional(profesional));
    }

    // 🔹 Eliminar un profesional
    @Operation(summary = "Eliminar un profesional")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesional(@PathVariable Long id) {
        if (!profesionalService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        profesionalService.eliminarProfesional(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Buscar profesionales por especialidad
    @Operation(summary = "Buscar profesionales por especialidad")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Profesional>> buscarPorEspecialidad(@PathVariable com.bluesoftware.city_connect_pro.entities.Especialidad especialidad) {
        return ResponseEntity.ok(profesionalService.buscarPorEspecialidad(especialidad));
    }

    // 🔹 Buscar profesionales por ciudad
    @Operation(summary = "Buscar profesionales por ciudad")
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Profesional>> buscarPorCiudad(@PathVariable String ciudad) {
        return ResponseEntity.ok(profesionalService.buscarPorCiudad(ciudad));
    }

    // 🔹 Buscar profesionales por especialidad y ciudad
    @Operation(summary = "Buscar profesionales por especialidad y ciudad")
    @GetMapping("/buscar")
    public ResponseEntity<List<Profesional>> buscarPorEspecialidadYCiudad(
            @RequestParam Especialidad especialidad,
            @RequestParam String ciudad) {
        return ResponseEntity.ok(profesionalService.buscarPorEspecialidadYCiudad(especialidad, ciudad));
    }
}
