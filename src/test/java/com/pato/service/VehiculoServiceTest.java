package com.pato.service;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.factory.VehiculoFactory;
import com.pato.helpers.mappers.TicketMapper;
import com.pato.helpers.mappers.VehiculoMapper;
import com.pato.model.Ticket;
import com.pato.model.Vehiculo;
import com.pato.model.enums.TipoVehiculo;
import com.pato.repository.ITicketRepository;
import com.pato.repository.IVehiculoRepository;
import com.pato.service.implementations.VehiculoService;
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
class VehiculoServiceTest {

    @Mock
    private IVehiculoRepository vehiculoRepository;

    @Mock
    private VehiculoMapper vehiculoMapper;

    @Mock
    private ITicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private VehiculoFactory vehiculoFactory;

    @InjectMocks
    private VehiculoService vehiculoService;

    private Vehiculo vehiculo;
    private VehiculoResponseDTO responseDTO;
    private VehiculoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        vehiculo = new Vehiculo(1L, TipoVehiculo.AUTO, "ABC123");
        responseDTO = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "ABC123");
        requestDTO = new VehiculoRequestDTO(TipoVehiculo.AUTO, "XYZ789");
    }

    @Test
    void getAllVehiculos_deberiaRetornarListaDeVehiculos() {
        when(vehiculoRepository.findAll()).thenReturn(List.of(vehiculo));
        when(vehiculoMapper.toResponseDto(vehiculo)).thenReturn(responseDTO);

        List<VehiculoResponseDTO> result = vehiculoService.getAllVehiculos();

        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).getPatente());
        verify(vehiculoRepository).findAll();
    }

    @Test
    void getVehiculo_existente_deberiaRetornarOptionalConDTO() {
        when(vehiculoRepository.findByPatente("ABC123")).thenReturn(Optional.of(vehiculo));
        when(vehiculoMapper.toResponseDto(vehiculo)).thenReturn(responseDTO);

        Optional<VehiculoResponseDTO> result = vehiculoService.getVehiculo("ABC123");

        assertTrue(result.isPresent());
        assertEquals("ABC123", result.get().getPatente());
        verify(vehiculoRepository).findByPatente("ABC123");
    }

    @Test
    void getVehiculo_inexistente_deberiaRetornarOptionalVacio() {
        when(vehiculoRepository.findByPatente("ZZZ000")).thenReturn(Optional.empty());

        Optional<VehiculoResponseDTO> result = vehiculoService.getVehiculo("ZZZ000");

        assertFalse(result.isPresent());
        verify(vehiculoRepository).findByPatente("ZZZ000");
    }

    @Test
    void editarVehiculo_existente_deberiaActualizarYRetornarDTO() {
        Vehiculo actualizado = new Vehiculo(1L, TipoVehiculo.MOTO, "XYZ789");
        VehiculoResponseDTO actualizadoDTO = new VehiculoResponseDTO(1L, TipoVehiculo.MOTO, "XYZ789");

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(vehiculoMapper.toEntity(requestDTO)).thenReturn(actualizado);
        when(vehiculoRepository.save(actualizado)).thenReturn(actualizado);
        when(vehiculoMapper.toResponseDto(actualizado)).thenReturn(actualizadoDTO);

        VehiculoResponseDTO result = vehiculoService.editarVehiculo(1L, requestDTO);

        assertEquals("XYZ789", result.getPatente());
        assertEquals(TipoVehiculo.MOTO, result.getTipo());
        verify(vehiculoRepository).save(actualizado);
    }

    @Test
    void editarVehiculo_inexistente_deberiaLanzarExcepcion() {
        when(vehiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> vehiculoService.editarVehiculo(99L, requestDTO));
    }

    @Test
    void getHistorialVehiculo_deberiaRetornarListaDeTickets() {
        Ticket ticket = new Ticket();
        TicketResponseDTO ticketDTO = new TicketResponseDTO();

        when(ticketRepository.findByVehiculoPatente("ABC123")).thenReturn(List.of(ticket));
        when(ticketMapper.toResponseDto(ticket)).thenReturn(ticketDTO);

        List<TicketResponseDTO> result = vehiculoService.getHistorialVehiculo("ABC123");

        assertEquals(1, result.size());
        verify(ticketRepository).findByVehiculoPatente("ABC123");
    }

    @Test
    void getEntityById_existente_deberiaRetornarEntidad() {
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        Vehiculo result = vehiculoService.getEntityById(1L);

        assertEquals("ABC123", result.getPatente());
        verify(vehiculoRepository).findById(1L);
    }

    @Test
    void getEntityById_inexistente_deberiaLanzarExcepcion() {
        when(vehiculoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> vehiculoService.getEntityById(10L));
    }

    @Test
    void getEntityByPatente_deberiaRetornarOptionalConEntidad() {
        when(vehiculoRepository.findByPatente("ABC123")).thenReturn(Optional.of(vehiculo));

        Optional<Vehiculo> result = vehiculoService.getEntityByPatente("ABC123");

        assertTrue(result.isPresent());
        assertEquals(vehiculo, result.get());
    }

    @Test
    void crearVehiculo_deberiaGuardarYRetornarEntidad() {
        Vehiculo nuevo = new Vehiculo(null, TipoVehiculo.MOTO, "ZZZ999");
        Vehiculo guardado = new Vehiculo(2L, TipoVehiculo.MOTO, "ZZZ999");

        when(vehiculoFactory.crearVehiculo(requestDTO)).thenReturn(nuevo);
        when(vehiculoRepository.save(nuevo)).thenReturn(guardado);

        Vehiculo result = vehiculoService.crearVehiculo(requestDTO);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("ZZZ999", result.getPatente());
        verify(vehiculoRepository).save(nuevo);
    }
}
