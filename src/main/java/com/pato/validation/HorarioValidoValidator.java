package com.pato.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HorarioValidoValidator implements ConstraintValidator<HorarioValido, LocalDateTime> {

//    private static final LocalTime HORA_INICIO = LocalTime.of(10, 0);
//    private static final LocalTime HORA_FIN = LocalTime.of(23, 0);

    private static final LocalTime HORA_INICIO = LocalTime.of(0, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(23, 59);

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return true; // Se permite null (por ejemplo, fechaHoraSalida antes de registrarse)

        LocalTime hora = value.toLocalTime();
        return !hora.isBefore(HORA_INICIO) && !hora.isAfter(HORA_FIN);
    }
}
