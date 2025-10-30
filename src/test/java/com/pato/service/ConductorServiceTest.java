package com.pato.service;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.factory.ConductorFactory;
import com.pato.helpers.mappers.ConductorMapper;
import com.pato.helpers.mappers.TicketMapper;
import com.pato.model.Conductor;
import com.pato.model.Ticket;
import com.pato.repository.IConductorRepository;
import com.pato.repository.ITicketRepository;
import com.pato.service.implementations.ConductorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConductorServiceTest {

    @Mock
    private IConductorRepository conductorRepository;

    @Mock
    private ITicketRepository ticketRepository;

    @Mock
    private ConductorMapper conductorMapper;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private ConductorFactory conductorFactory;

    @InjectMocks
    private ConductorService conductorService;

    private Conductor conductor;
    private ConductorRequestDTO requestDTO;
    private ConductorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        conductor = new Conductor(1L, "Juan", "Pérez", "12345678");
        requestDTO = new ConductorRequestDTO("Juan", "Pérez", "12345678");
        responseDTO = new ConductorResponseDTO(1L, "Juan", "Pérez", "12345678");
    }

    @Test
    void getAllConductores_deberiaRetornarListaDeConductores() {
        when(conductorRepository.findAll()).thenReturn(List.of(conductor));
        when(conductorMapper.toResponseDto(conductor)).thenReturn(responseDTO);

        List<ConductorResponseDTO> result = conductorService.getAllConductores();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        verify(conductorRepository).findAll();
    }

    @Test
    void getConductor_existente_deberiaRetornarOptionalConDTO() {
        when(conductorRepository.findByDni("12345678")).thenReturn(Optional.of(conductor));
        when(conductorMapper.toResponseDto(conductor)).thenReturn(responseDTO);

        Optional<ConductorResponseDTO> result = conductorService.getConductor("12345678");

        assertTrue(result.isPresent());
        assertEquals("12345678", result.get().getDni());
        verify(conductorRepository).findByDni("12345678");
    }

    @Test
    void getConductor_inexistente_deberiaRetornarOptionalVacio() {
        when(conductorRepository.findByDni("00000000")).thenReturn(Optional.empty());

        Optional<ConductorResponseDTO> result = conductorService.getConductor("00000000");

        assertFalse(result.isPresent());
        verify(conductorRepository).findByDni("00000000");
    }

    @Test
    void editarConductor_existente_deberiaActualizarYRetornarDTO() {
        Conductor actualizado = new Conductor(1L, "Pedro", "Gómez", "87654321");
        ConductorResponseDTO actualizadoDTO = new ConductorResponseDTO(1L, "Pedro", "Gómez", "87654321");

        when(conductorRepository.findById(1L)).thenReturn(Optional.of(conductor));
        when(conductorMapper.toEntity(requestDTO)).thenReturn(actualizado);
        when(conductorRepository.save(actualizado)).thenReturn(actualizado);
        when(conductorMapper.toResponseDto(actualizado)).thenReturn(actualizadoDTO);

        ConductorResponseDTO result = conductorService.editarConductor(1L, requestDTO);

        assertEquals("Pedro", result.getNombre());
        verify(conductorRepository).save(actualizado);
    }

    @Test
    void editarConductor_inexistente_deberiaLanzarExcepcion() {
        when(conductorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> conductorService.editarConductor(99L, requestDTO));
    }

    @Test
    void getHistorialConductor_deberiaRetornarListaDeTickets() {
        Ticket ticket = new Ticket();
        TicketResponseDTO ticketDTO = new TicketResponseDTO();

        when(ticketRepository.findByConductorDni("12345678")).thenReturn(List.of(ticket));
        when(ticketMapper.toResponseDto(ticket)).thenReturn(ticketDTO);

        List<TicketResponseDTO> result = conductorService.getHistorialConductor("12345678");

        assertEquals(1, result.size());
        verify(ticketRepository).findByConductorDni("12345678");
    }

    @Test
    void getEntityById_existente_deberiaRetornarEntidad() {
        when(conductorRepository.findById(1L)).thenReturn(Optional.of(conductor));

        Conductor result = conductorService.getEntityById(1L);

        assertEquals("Juan", result.getNombre());
        verify(conductorRepository).findById(1L);
    }

    @Test
    void getEntityById_inexistente_deberiaLanzarExcepcion() {
        when(conductorRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> conductorService.getEntityById(5L));
    }

    @Test
    void getEntityByDni_deberiaRetornarOptionalConEntidad() {
        when(conductorRepository.findByDni("12345678")).thenReturn(Optional.of(conductor));

        Optional<Conductor> result = conductorService.getEntityByDni("12345678");

        assertTrue(result.isPresent());
        assertEquals(conductor, result.get());
    }

    @Test
    void crearConductor_deberiaGuardarYRetornarEntidad() {
        Conductor nuevo = new Conductor(null, "Juan", "Pérez", "12345678");
        Conductor guardado = new Conductor(1L, "Juan", "Pérez", "12345678");

        when(conductorFactory.crearConductor(requestDTO)).thenReturn(nuevo);
        when(conductorRepository.save(nuevo)).thenReturn(guardado);

        Conductor result = conductorService.crearConductor(requestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(conductorRepository).save(nuevo);
    }
}
