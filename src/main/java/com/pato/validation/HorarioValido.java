package com.pato.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HorarioValidoValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HorarioValido {
    String message() default "La hora debe estar entre las 10:00 y las 23:00";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
