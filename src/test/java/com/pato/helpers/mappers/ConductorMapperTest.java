package com.pato.helpers.mappers;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.model.Conductor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class ConductorMapperTest {

    private ConductorMapper conductorMapper;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        conductorMapper = new ConductorMapper(modelMapper);
    }

    @Test
    void toResponseDto_deberiaMapearCorrectamente() {
        Conductor conductor = new Conductor(1L, "Juan", "Pérez", "12345678");

        ConductorResponseDTO dto = conductorMapper.toResponseDto(conductor);

        assertNotNull(dto);
        assertEquals(conductor.getDni(), dto.getDni());
        assertEquals(conductor.getNombre(), dto.getNombre());
        assertEquals(conductor.getApellido(), dto.getApellido());
    }

    @Test
    void toEntity_desdeRequestDTO_deberiaMapearCorrectamente() {
        ConductorRequestDTO dto = new ConductorRequestDTO("María", "López", "87654321");

        Conductor conductor = conductorMapper.toEntity(dto);

        assertNotNull(conductor);
        assertEquals(dto.getDni(), conductor.getDni());
        assertEquals(dto.getNombre(), conductor.getNombre());
        assertEquals(dto.getApellido(), conductor.getApellido());
    }

    @Test
    void toEntity_desdeResponseDTO_deberiaMapearCorrectamente() {
        ConductorResponseDTO dto = new ConductorResponseDTO(2L, "Carlos", "García", "11122333");

        Conductor conductor = conductorMapper.toEntity(dto);

        assertNotNull(conductor);
        assertEquals(dto.getId(), conductor.getId());
        assertEquals(dto.getDni(), conductor.getDni());
    }
}
