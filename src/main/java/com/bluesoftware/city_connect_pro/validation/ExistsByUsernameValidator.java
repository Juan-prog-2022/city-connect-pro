package com.bluesoftware.city_connect_pro.validation;

import com.bluesoftware.city_connect_pro.repositories.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistsByUsernameValidator implements ConstraintValidator<ExistsByUsername, String> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !usuarioRepository.existsByUsername(username); // Retorna false si el username ya existe
    }
}