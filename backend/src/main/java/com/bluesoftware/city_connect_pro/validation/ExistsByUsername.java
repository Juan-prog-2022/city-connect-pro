package com.bluesoftware.city_connect_pro.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExistsByUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByUsername {
    String message() default "El nombre de usuario ya existe en el sistema. Escoja otro.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}