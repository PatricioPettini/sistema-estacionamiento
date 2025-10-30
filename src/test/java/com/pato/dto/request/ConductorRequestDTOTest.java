package com.pato.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConductorRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deberiaFallarCuandoNombreEsVacio() {
        ConductorRequestDTO dto = new ConductorRequestDTO();
        dto.setNombre("");
        dto.setApellido("Pérez");
        dto.setDni("12345678");

        Set<ConstraintViolation<ConductorRequestDTO>> violaciones = validator.validate(dto);

        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("Debe ingresar el nombre del conductor")));
    }

    @Test
    void deberiaFallarCuandoDniNoTieneLongitudValida() {
        ConductorRequestDTO dto = new ConductorRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setDni("123");

        Set<ConstraintViolation<ConductorRequestDTO>> violaciones = validator.validate(dto);

        assertFalse(violaciones.isEmpty());
        assertTrue(violaciones.stream()
                .anyMatch(v -> v.getMessage().contains("debe tener entre 7 y 8 dígitos")));
    }

    @Test
    void deberiaSerValidoCuandoTodosLosCamposSonCorrectos() {
        ConductorRequestDTO dto = new ConductorRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setDni("12345678");

        Set<ConstraintViolation<ConductorRequestDTO>> violaciones = validator.validate(dto);

        assertTrue(violaciones.isEmpty());
    }
}
