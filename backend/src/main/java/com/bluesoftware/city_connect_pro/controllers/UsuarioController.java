package com.bluesoftware.city_connect_pro.controllers;

import com.bluesoftware.city_connect_pro.dtos.UsuarioResponseDTO;
import com.bluesoftware.city_connect_pro.entities.Role;
import com.bluesoftware.city_connect_pro.entities.Usuario;
import com.bluesoftware.city_connect_pro.mapper.UsuarioMapper;
import com.bluesoftware.city_connect_pro.services.RoleService;
import com.bluesoftware.city_connect_pro.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UsuarioMapper usuarioMapper;

    // 🔹 Listar todos los usuarios (solo para ADMIN)
    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<?> listarUsuarios(Authentication auth) {
        if (auth == null || !tieneRolAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden ver todos los usuarios.");
        }

        // Obtener la lista de usuarios desde el servicio
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        // Mapear los usuarios a DTOs
        List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
        .map(usuarioMapper::usuarioToDto)
        .toList();

        return ResponseEntity.ok(usuariosDTO);
    }


    // 🔹 Ver perfil por ID (solo el usuario o el admin)
    @Operation(summary = "Ver perfil del usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long id, Authentication auth) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorId(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        if (!usuario.getUsername().equals(auth.getName()) && !tieneRolAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a este perfil");
        }

        return ResponseEntity.ok(usuario);
    }

    // 🔹 Buscar usuario por email (uso interno o admin)
    @Operation(summary = "Buscar usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // 🔹 Buscar usuario por DNI (uso interno o admin)
    @Operation(summary = "Buscar usuario por DNI")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Usuario> buscarPorDni(@PathVariable String dni) {
        Usuario usuario = usuarioService.buscarPorDni(dni);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    // 🔹 Crear usuario con rol explícito (uso interno solo admin)
    @PostMapping
    @Operation(summary = "Crear usuario con rol (uso interno)")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario, Authentication auth) {
        if (!tieneRolAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo el ADMIN puede crear usuarios manualmente");
        }

        if (usuarioService.buscarPorEmail(usuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya está registrado");
        }

        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // 🔹 Registro de nuevo usuario
    @Operation(summary = "Registrar nuevo usuario CLIENTE o PROFESIONAL")
    @PostMapping("/registrarse")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, Object> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
        }

        // Asignar rol CLIENTE por defecto si no se especificó ninguno
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            Role rolCliente = roleService.buscarPorNombre("ROLE_CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));
            usuario.setRoles(List.of(rolCliente));
        }

        Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // 🔹 Actualizar usuario (solo su propio perfil o admin)
    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario, Authentication auth) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorId(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuarioBD = usuarioOpt.get();
        if (!usuarioBD.getUsername().equals(auth.getName()) && !tieneRolAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para actualizar este perfil");
        }

        usuario.setId(id); // asegura que se actualice el correcto
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    // 🔹 Eliminar usuario (solo admin)
    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, Authentication auth) {
        if (!tieneRolAdmin(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo el ADMIN puede eliminar usuarios");
        }

        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
        if (usuario.isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 🔸 Utilidad para verificar si el usuario autenticado es ADMIN
    private boolean tieneRolAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}
