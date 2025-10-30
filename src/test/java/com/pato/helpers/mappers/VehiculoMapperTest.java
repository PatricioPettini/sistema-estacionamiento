package com.pato.helpers.mappers;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.Vehiculo;
import com.pato.model.enums.TipoVehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoMapperTest {

    private VehiculoMapper vehiculoMapper;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        vehiculoMapper = new VehiculoMapper(modelMapper);
    }

    @Test
    void toResponseDto_deberiaMapearCorrectamente() {
        Vehiculo vehiculo = new Vehiculo(1L, TipoVehiculo.AUTO, "ABC123");

        VehiculoResponseDTO dto = vehiculoMapper.toResponseDto(vehiculo);

        assertNotNull(dto);
        assertEquals(vehiculo.getPatente(), dto.getPatente());
        assertEquals(vehiculo.getTipo(), dto.getTipo());
    }

    @Test
    void toEntity_desdeRequestDTO_deberiaConvertirPatenteAMayusculas() {
        VehiculoRequestDTO dto = new VehiculoRequestDTO(TipoVehiculo.MOTO, "abc999");

        Vehiculo vehiculo = vehiculoMapper.toEntity(dto);

        assertNotNull(vehiculo);
        assertEquals("ABC999", vehiculo.getPatente());
        assertEquals(TipoVehiculo.MOTO, vehiculo.getTipo());
    }

    @Test
    void toEntity_desdeResponseDTO_deberiaMapearCorrectamente() {
        VehiculoResponseDTO dto = new VehiculoResponseDTO(1L, TipoVehiculo.BICICLETA, "BIKE01");

        Vehiculo vehiculo = vehiculoMapper.toEntity(dto);

        assertNotNull(vehiculo);
        assertEquals(dto.getPatente(), vehiculo.getPatente());
        assertEquals(dto.getTipo(), vehiculo.getTipo());
    }
}
