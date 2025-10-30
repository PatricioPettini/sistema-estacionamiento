package com.pato.model;

import com.pato.model.enums.TipoVehiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehiculoTest {

    @Test
    void deberiaCrearVehiculoConBuilder() {
        Vehiculo vehiculo = Vehiculo.builder()
                .tipo(TipoVehiculo.AUTO)
                .patente("ABC123")
                .build();

        assertEquals(TipoVehiculo.AUTO, vehiculo.getTipo());
        assertEquals("ABC123", vehiculo.getPatente());
    }

    @Test
    void deberiaPermitirUsarSettersYGetters() {
        Vehiculo v = new Vehiculo();
        v.setTipo(TipoVehiculo.MOTO);
        v.setPatente("XYZ789");

        assertEquals(TipoVehiculo.MOTO, v.getTipo());
        assertEquals("XYZ789", v.getPatente());
    }
}
