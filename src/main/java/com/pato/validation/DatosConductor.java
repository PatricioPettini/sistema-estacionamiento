package com.pato.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DatosConductorValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DatosConductor {
    String message() default "Los datos del conductor no son válidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
