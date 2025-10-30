package com.pato.dto.request;

import com.pato.model.enums.TipoVehiculo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehiculoRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deberiaFallarCuandoTipoEsNulo() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setTipo(null);
        dto.setPatente("ABC123");

        Set<ConstraintViolation<VehiculoRequestDTO>> violaciones = validator.validate(dto);

        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("El tipo de vehículo es obligatorio")));
    }

    @Test
    void deberiaSerValidoCuandoTipoYPantenteSonCorrectos() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setTipo(TipoVehiculo.AUTO);
        dto.setPatente("abc123");

        Set<ConstraintViolation<VehiculoRequestDTO>> violaciones = validator.validate(dto);
        assertTrue(violaciones.isEmpty());
    }
}
