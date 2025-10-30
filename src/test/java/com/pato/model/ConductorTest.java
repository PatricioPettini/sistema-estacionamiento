package com.pato.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConductorTest {

    @Test
    void deberiaCrearConductorConBuilder() {
        Conductor conductor = Conductor.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .build();

        assertEquals("Juan", conductor.getNombre());
        assertEquals("Pérez", conductor.getApellido());
        assertEquals("12345678", conductor.getDni());
    }

    @Test
    void deberiaPermitirSettersYGetters() {
        Conductor c = new Conductor();
        c.setNombre("Ana");
        c.setApellido("García");
        c.setDni("87654321");

        assertEquals("Ana", c.getNombre());
        assertEquals("García", c.getApellido());
        assertEquals("87654321", c.getDni());
    }
}
