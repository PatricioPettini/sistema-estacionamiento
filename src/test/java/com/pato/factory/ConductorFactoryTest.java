package com.pato.factory;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.model.Conductor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConductorFactoryTest {

    private ConductorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ConductorFactory(new ModelMapper());
    }

    @Test
    void crearConductor_deberiaMapearCamposCorrectamente() {
        // Arrange
        ConductorRequestDTO dto = new ConductorRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setDni("12345678");

        // Act
        Conductor conductor = factory.crearConductor(dto);

        // Assert
        assertNotNull(conductor);
        assertEquals("Juan", conductor.getNombre());
        assertEquals("Pérez", conductor.getApellido());
        assertEquals("12345678", conductor.getDni());
    }
}
