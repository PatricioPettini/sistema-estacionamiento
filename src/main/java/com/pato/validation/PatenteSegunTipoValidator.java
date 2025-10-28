package com.pato.validation;

import com.pato.dto.request.VehiculoRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PatenteSegunTipoValidator implements ConstraintValidator<PatenteSegunTipo, VehiculoRequestDTO> {

    @Override
    public boolean isValid(VehiculoRequestDTO vehiculo, ConstraintValidatorContext context) {
        if (vehiculo == null) return true; // evita NPEs

        String tipo = vehiculo.getTipo() != null ? vehiculo.getTipo().name() : null;
        String patente = vehiculo.getPatente();

        // ✅ Caso 1: Bicicleta → la patente puede estar vacía o null
        if ("BICICLETA".equalsIgnoreCase(tipo)) {
            if (patente != null && !patente.trim().isEmpty()) {
                vehiculo.setPatente(null);
            }
            return true;
        }

        // ✅ Caso 2: Otros tipos → la patente es obligatoria y debe cumplir formato
        if (patente == null || patente.trim().isEmpty()) {
            agregarMensaje(context, "La patente es obligatoria para vehículos que no sean bicicletas");
            return false;
        }

        if (!patente.matches("^[A-Za-z0-9]{6,7}$")) {
            agregarMensaje(context, "La patente debe tener entre 6 y 7 caracteres alfanuméricos sin espacios ni símbolos (ejemplo: AA222BB)");
            return false;
        }

        return true;
    }

    // 🔧 Método auxiliar para personalizar el mensaje de error
    private void agregarMensaje(ConstraintValidatorContext context, String mensaje) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(mensaje)
                .addPropertyNode("patente")
                .addConstraintViolation();
    }
}

