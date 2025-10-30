package com.pato.factory;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.model.Vehiculo;
import com.pato.model.enums.TipoVehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehiculoFactoryTest {

    private VehiculoFactory factory;

    @BeforeEach
    void setUp() {
        factory = new VehiculoFactory(new ModelMapper());
    }

    @Test
    void crearVehiculo_deberiaConvertirPatenteAMayusculasSiNoEsBicicleta() {
        // Arrange
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setTipo(TipoVehiculo.AUTO);
        dto.setPatente("abc123");

        // Act
        Vehiculo vehiculo = factory.crearVehiculo(dto);

        // Assert
        assertNotNull(vehiculo);
        assertEquals(TipoVehiculo.AUTO, vehiculo.getTipo());
        assertEquals("ABC123", vehiculo.getPatente());
    }

    @Test
    void crearVehiculo_noDeberiaModificarPatenteSiEsBicicleta() {
        // Arrange
        VehiculoRequestDTO dto = new VehiculoRequestDTO();
        dto.setTipo(TipoVehiculo.BICICLETA);
        dto.setPatente("sinpatente");

        // Act
        Vehiculo vehiculo = factory.crearVehiculo(dto);

        // Assert
        assertNotNull(vehiculo);
        assertEquals(TipoVehiculo.BICICLETA, vehiculo.getTipo());
        assertEquals("sinpatente", vehiculo.getPatente());
    }
}
