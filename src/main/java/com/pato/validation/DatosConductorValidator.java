package com.pato.validation;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.model.Conductor;
import com.pato.service.interfaces.IConductorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatosConductorValidator implements ConstraintValidator<DatosConductor, ConductorRequestDTO> {

    private final IConductorService conductorService;

    @Override
    public boolean isValid(ConductorRequestDTO dto, ConstraintValidatorContext context) {
        String dni = dto.getDni();

        if (dni == null || dni.isBlank()) {
            agregarMensaje(context, "El DNI es obligatorio");
            return false;
        }

        // Verifico si el conductor ya existe
        boolean existe = conductorService.getEntityByDni(dni).isPresent();

        // Si no existe, debe ingresar nombre y apellido válidos
        if (!existe) {
            if (dto.getNombre() == null || dto.getNombre().isBlank()) {
                agregarMensaje(context, "Debe ingresar el nombre del conductor");
                return false;
            }

            if (dto.getApellido() == null || dto.getApellido().isBlank()) {
                agregarMensaje(context, "Debe ingresar el apellido del conductor");
                return false;
            }

            if (!dto.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
                agregarMensaje(context, "El nombre solo puede contener letras y espacios");
                return false;
            }

            if (!dto.getApellido().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
                agregarMensaje(context, "El apellido solo puede contener letras y espacios");
                return false;
            }
        }

        return true;
    }

    private void agregarMensaje(ConstraintValidatorContext context, String mensaje) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(mensaje)
                .addPropertyNode("conductor")
                .addConstraintViolation();
    }
}