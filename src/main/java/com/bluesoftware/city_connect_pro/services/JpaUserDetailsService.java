
package com.bluesoftware.city_connect_pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluesoftware.city_connect_pro.entities.Usuario;
import com.bluesoftware.city_connect_pro.repositories.UsuarioRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);

        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Usuario %s no encontrado en el sistema!", username));
        }

        Usuario user = usuario.get();

        if (!user.getActivo()) {
            throw new UsernameNotFoundException("La cuenta está desactivada. Contacte con el administrador.");
        }

        // Mapear los Roles de usuario y convertirlos en una lista de GrantedAuthority
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                .toList();


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getActivo(),
                true,
                true,
                true,
                authorities // lista porque necesita Collection<GrantedAuthority>
        );
    }
}
