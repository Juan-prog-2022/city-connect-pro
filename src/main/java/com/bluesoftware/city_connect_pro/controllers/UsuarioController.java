package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.entities.TipoUsuario;
import com.bluesoftware.city_connect_pro.entities.Usuario;
import com.bluesoftware.city_connect_pro.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 🔹 Listar todos los usuarios
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // 🔹 Buscar usuario por ID
    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Buscar usuario por email
    @Operation(summary = "Buscar usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // 🔹 Buscar usuario por DNI
    @Operation(summary = "Buscar usuario por DNI")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Usuario> buscarPorDni(@PathVariable String dni) {
        Usuario usuario = usuarioService.buscarPorDni(dni);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // 🔹 Crear nuevo usuario Admin
    @Operation(summary = "Crear nuevo usuario Admin (Solo para uso interno)")
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (usuario.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("No tienes permisos para realizar esta acción");   
        }

        if (usuarioService.buscarPorEmail(usuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("El email ya está registrado");
        }

        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.status(201).body(nuevoUsuario);
    }

    // 🔹 Registrar nuevo usuario comun o profesional
    @Operation(summary = "Registrar nuevo usuario CLIENTE o PROFESIONAL")
    @PostMapping("/registrarse")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {

        // Validar errores de entrada
        if (result.hasErrors()) {
            Map<String, Object> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> {
                errores.put(err.getField(), err.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(result.getAllErrors());
        }

        // Evitar que alguien intente registrarse como ADMINISTRADOR
        if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "No puedes registrarte como ADMINISTRADOR."));
        }

        // Verificar si el email ya existe
        if (usuarioService.buscarPorEmail(usuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "El email ya está registrado."));
        }

        // Verificar si el DNI ya existe
        if (usuarioService.buscarPorDni(usuario.getDni()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "El DNI ya está registrado."));
        }

        // Verificar si el username ya existe
        if (usuarioService.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "El nombre de usuario ya está en uso."));
        }

        // Si no se especifica un tipo de usuario, se asume que es CLIENTE
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        }

        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // 🔹 Actualizar usuario
    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.buscarUsuarioPorId(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);  // Para asegurar que el ID no cambie en la actualización
            Usuario usuarioActualizado = usuarioService.guardarUsuario(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // 🔹 Eliminar usuario
    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
        if (usuario.isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
