package com.pato.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PatenteSegunTipoValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PatenteSegunTipo {
    String message() default "La patente es obligatoria salvo que el vehículo sea una bicicleta";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
