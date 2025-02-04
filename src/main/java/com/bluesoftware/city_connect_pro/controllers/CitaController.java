package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.entities.Cita;
import com.bluesoftware.city_connect_pro.services.CitaService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier origen
public class CitaController {

    @Autowired
    private CitaService citaService;

    // 🔹 Obtener todas las citas
    @Operation(summary = "Listar todas las citas")
    @GetMapping
    public ResponseEntity<List<Cita>> listarCitas() {
        return ResponseEntity.ok(citaService.listarCitas());
    }

    // 🔹 Obtener una cita por ID
    @Operation(summary = "Obtener una cita por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        Optional<Cita> cita = citaService.buscarCitaPorId(id);
        return cita.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Crear una nueva cita
    @Operation(summary = "Crear una nueva cita")
    @PostMapping
    public ResponseEntity<Cita> crearCita(@RequestBody Cita cita) {
        Cita nuevaCita = citaService.guardarCita(cita);
        return ResponseEntity.ok(nuevaCita);
    }

    // 🔹 Actualizar una cita
    @Operation(summary = "Actualizar una cita")
    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita cita) {
        if (!citaService.buscarCitaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        cita.setId(id);
        return ResponseEntity.ok(citaService.guardarCita(cita));
    }

    // 🔹 Eliminar una cita
    @Operation(summary = "Eliminar una cita")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        if (!citaService.buscarCitaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Buscar citas por profesional
    @Operation(summary = "Buscar citas por profesional")
    @GetMapping("/profesional/{id}")
    public ResponseEntity<List<Cita>> buscarPorProfesional(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorProfesional(id));
    }

    // 🔹 Buscar citas por usuario
    @Operation(summary = "Buscar citas por usuario")
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Cita>> buscarPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorUsuario(id));
    }
}
