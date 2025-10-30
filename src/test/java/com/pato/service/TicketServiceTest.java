package com.pato.service;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.factory.TicketFactory;
import com.pato.helpers.mappers.TicketMapper;
import com.pato.model.Conductor;
import com.pato.model.Ticket;
import com.pato.model.Vehiculo;
import com.pato.model.enums.EstadoTicket;
import com.pato.model.enums.TipoVehiculo;
import com.pato.repository.ITicketRepository;
import com.pato.service.implementations.TicketService;
import com.pato.validation.TicketValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private ITicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketFactory ticketFactory;

    @Mock
    private TicketValidator ticketValidator;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private TicketResponseDTO ticketResponse;
    private TicketRequestDTO ticketRequest;

    @BeforeEach
    void setUp() {
        Conductor conductor = new Conductor(1L, "Juan", "Pérez", "12345678");
        Vehiculo vehiculo = new Vehiculo(1L, TipoVehiculo.AUTO, "ABC123");

        ticket = new Ticket(
                1L,
                vehiculo,
                conductor,
                LocalDateTime.now(),
                null,
                EstadoTicket.EN_CURSO,
                "Sin observaciones"
        );

        ticketResponse = new TicketResponseDTO();
        ticketResponse.setId(1L);
        ticketResponse.setEstadoTicket(EstadoTicket.EN_CURSO);

        ticketRequest = new TicketRequestDTO();
        ticketRequest.setVehiculo(new VehiculoRequestDTO(TipoVehiculo.AUTO, "ABC123"));
        ticketRequest.setConductor(new ConductorRequestDTO("Juan", "Pérez", "12345678"));
    }

    @Test
    void getTicket_existente_deberiaRetornarDTO() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toResponseDto(ticket)).thenReturn(ticketResponse);

        TicketResponseDTO result = ticketService.getTicket(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ticketRepository).findById(1L);
        verify(ticketMapper).toResponseDto(ticket);
    }

    @Test
    void getTicket_inexistente_deberiaLanzarExcepcion() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ticketService.getTicket(99L));
    }

    @Test
    void getAllTickets_deberiaRetornarListaDeTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        when(ticketMapper.toResponseDto(ticket)).thenReturn(ticketResponse);

        List<TicketResponseDTO> result = ticketService.getAllTickets();

        assertEquals(1, result.size());
        assertEquals(EstadoTicket.EN_CURSO, result.get(0).getEstadoTicket());
        verify(ticketRepository).findAll();
    }

    @Test
    void crearTicket_deberiaGuardarYRetornarDTO() {
        // Arrange
        Conductor conductor = new Conductor(1L, "Juan", "Pérez", "12345678");
        Vehiculo vehiculo = new Vehiculo(1L, TipoVehiculo.AUTO, "ABC123");
        Ticket nuevo = new Ticket(null, vehiculo, conductor, LocalDateTime.now(), null, EstadoTicket.EN_CURSO, null);
        Ticket guardado = new Ticket(10L, vehiculo, conductor, LocalDateTime.now(), null, EstadoTicket.EN_CURSO, null);

        when(ticketFactory.crearTicket(ticketRequest)).thenReturn(nuevo);
        when(ticketRepository.save(nuevo)).thenReturn(guardado);
        when(ticketMapper.toResponseDto(guardado)).thenReturn(ticketResponse);

        // Act
        TicketResponseDTO result = ticketService.crearTicket(ticketRequest);

        // Assert
        assertNotNull(result);
        verify(ticketFactory).crearTicket(ticketRequest);
        verify(ticketRepository).save(nuevo);
        verify(ticketMapper).toResponseDto(guardado);
    }

    @Test
    void editarTicket_existente_deberiaActualizarYRetornarDTO() {
        Ticket actualizado = new Ticket(1L, ticket.getVehiculo(), ticket.getConductor(),
                ticket.getFechaHoraEntrada(), null, EstadoTicket.EN_CURSO, "OK");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketValidator).validarTicketFinalizado(1L, EstadoTicket.EN_CURSO);
        when(ticketFactory.actualizarDesdeDTO(ticket, ticketRequest)).thenReturn(actualizado);
        when(ticketRepository.save(actualizado)).thenReturn(actualizado);
        when(ticketMapper.toResponseDto(actualizado)).thenReturn(ticketResponse);

        TicketResponseDTO result = ticketService.editarTicket(1L, ticketRequest);

        assertNotNull(result);
        verify(ticketValidator).validarTicketFinalizado(1L, EstadoTicket.EN_CURSO);
        verify(ticketRepository).save(actualizado);
    }

    @Test
    void editarTicket_inexistente_deberiaLanzarExcepcion() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ticketService.editarTicket(99L, ticketRequest));
    }

    @Test
    void salidaVehiculo_existente_deberiaFinalizarTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketValidator).validarTicketFinalizado(1L, EstadoTicket.EN_CURSO);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.toResponseDto(ticket)).thenReturn(ticketResponse);

        TicketResponseDTO result = ticketService.salidaVehiculo(1L, "Todo ok");

        assertNotNull(result);
        verify(ticketValidator).validarTicketFinalizado(1L, EstadoTicket.EN_CURSO);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void salidaVehiculo_inexistente_deberiaLanzarExcepcion() {
        when(ticketRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ticketService.salidaVehiculo(404L, ""));
    }

    @Test
    void getEntityById_existente_deberiaRetornarTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.getEntityById(1L);

        assertEquals(1L, result.getId());
        verify(ticketRepository).findById(1L);
    }

    @Test
    void getEntityById_inexistente_deberiaLanzarExcepcion() {
        when(ticketRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ticketService.getEntityById(9L));
    }
}
