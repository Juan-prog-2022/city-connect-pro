package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import com.bluesoftware.city_connect_pro.entities.Usuario;

public interface UsuarioService {
    List<Usuario> listarUsuarios();
    Optional<Usuario> buscarUsuarioPorId(Long id);
    Usuario guardarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Long id, Usuario usuarioDetalles);
    void eliminarUsuario(Long id);
    Usuario buscarPorEmail(String email);
    Usuario buscarPorDni(String dni);
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}
