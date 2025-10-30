package com.pato.factory;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.helpers.mappers.ConductorMapper;
import com.pato.helpers.mappers.VehiculoMapper;
import com.pato.model.Conductor;
import com.pato.model.Ticket;
import com.pato.model.Vehiculo;
import com.pato.model.enums.EstadoTicket;
import com.pato.model.enums.TipoVehiculo;
import com.pato.service.interfaces.IConductorService;
import com.pato.service.interfaces.IVehiculoService;
import com.pato.validation.TicketValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketFactoryTest {

    @Mock
    private IConductorService conductorService;

    @Mock
    private IVehiculoService vehiculoService;

    @Mock
    private ConductorMapper conductorMapper;

    @Mock
    private VehiculoMapper vehiculoMapper;

    @Mock
    private TicketValidator ticketValidator;

    @InjectMocks
    private TicketFactory ticketFactory;

    private Conductor conductorExistente;
    private Vehiculo vehiculoExistente;
    private ConductorRequestDTO conductorRequest;
    private VehiculoRequestDTO vehiculoRequest;
    private TicketRequestDTO ticketRequest;

    @BeforeEach
    void setUp() {
        conductorExistente = new Conductor(1L, "Juan", "Pérez", "12345678");
        vehiculoExistente = new Vehiculo(1L, TipoVehiculo.AUTO, "ABC123");

        conductorRequest = new ConductorRequestDTO("Juan", "Pérez", "12345678");
        vehiculoRequest = new VehiculoRequestDTO(TipoVehiculo.AUTO, "ABC123");

        ticketRequest = new TicketRequestDTO();
        ticketRequest.setConductor(conductorRequest);
        ticketRequest.setVehiculo(vehiculoRequest);
    }

    @Test
    void crearTicket_conConductorYVehiculoExistentes_deberiaCrearTicketCorrectamente() {
        when(conductorService.getEntityByDni("12345678")).thenReturn(Optional.of(conductorExistente));
        when(vehiculoService.getEntityByPatente("ABC123")).thenReturn(Optional.of(vehiculoExistente));

        Ticket result = ticketFactory.crearTicket(ticketRequest);

        assertNotNull(result);
        assertEquals(conductorExistente, result.getConductor());
        assertEquals(vehiculoExistente, result.getVehiculo());
        assertEquals(EstadoTicket.EN_CURSO, result.getEstadoTicket());
        assertNotNull(result.getFechaHoraEntrada());

        verify(ticketValidator).validarConductorEnCurso("12345678", EstadoTicket.EN_CURSO);
        verify(ticketValidator).validarVehiculoEnCurso("ABC123", EstadoTicket.EN_CURSO);
    }

    @Test
    void crearTicket_conNuevosConductorYVehiculo_deberiaInvocarCreacion() {
        when(conductorService.getEntityByDni("12345678")).thenReturn(Optional.empty());
        when(vehiculoService.getEntityByPatente("ABC123")).thenReturn(Optional.empty());
        when(conductorService.crearConductor(conductorRequest)).thenReturn(conductorExistente);
        when(vehiculoService.crearVehiculo(vehiculoRequest)).thenReturn(vehiculoExistente);

        Ticket result = ticketFactory.crearTicket(ticketRequest);

        assertEquals(EstadoTicket.EN_CURSO, result.getEstadoTicket());
        verify(conductorService).crearConductor(conductorRequest);
        verify(vehiculoService).crearVehiculo(vehiculoRequest);
    }

    @Test
    void crearTicket_conVehiculoBicicleta_noDeberiaValidarVehiculoEnCurso() {
        vehiculoRequest.setTipo(TipoVehiculo.BICICLETA);
        vehiculoExistente.setTipo(TipoVehiculo.BICICLETA);

        when(conductorService.getEntityByDni("12345678")).thenReturn(Optional.of(conductorExistente));
        when(vehiculoService.getEntityByPatente("ABC123")).thenReturn(Optional.of(vehiculoExistente));

        Ticket result = ticketFactory.crearTicket(ticketRequest);

        assertNotNull(result);
        verify(ticketValidator, never()).validarVehiculoEnCurso(anyString(), any());
    }

    @Test
    void actualizarDesdeDTO_conDatosModificados_deberiaActualizarEntidades() {
        Ticket ticketExistente = Ticket.builder()
                .id(1L)
                .conductor(conductorExistente)
                .vehiculo(vehiculoExistente)
                .fechaHoraEntrada(LocalDateTime.now())
                .estadoTicket(EstadoTicket.EN_CURSO)
                .build();

        ConductorRequestDTO conductorNuevo = new ConductorRequestDTO("Pedro", "Gómez", "87654321");
        VehiculoRequestDTO vehiculoNuevo = new VehiculoRequestDTO(TipoVehiculo.MOTO, "XYZ999");

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setConductor(conductorNuevo);
        dto.setVehiculo(vehiculoNuevo);

        Conductor conductorActualizado = new Conductor(1L, "Pedro", "Gómez", "87654321");
        Vehiculo vehiculoActualizado = new Vehiculo(1L, TipoVehiculo.MOTO, "XYZ999");
        ConductorResponseDTO conductorResponse = new ConductorResponseDTO(1L, "Pedro", "Gómez", "87654321");
        VehiculoResponseDTO vehiculoResponse = new VehiculoResponseDTO(1L, TipoVehiculo.MOTO, "XYZ999");

        when(conductorService.getEntityById(1L)).thenReturn(conductorExistente);
        when(vehiculoService.getEntityById(1L)).thenReturn(vehiculoExistente);
        when(conductorService.editarConductor(1L, conductorNuevo)).thenReturn(conductorResponse);
        when(vehiculoService.editarVehiculo(1L, vehiculoNuevo)).thenReturn(vehiculoResponse);
        when(conductorMapper.toEntity(conductorResponse)).thenReturn(conductorActualizado);
        when(vehiculoMapper.toEntity(vehiculoResponse)).thenReturn(vehiculoActualizado);

        Ticket actualizado = ticketFactory.actualizarDesdeDTO(ticketExistente, dto);

        assertNotNull(actualizado);
        assertEquals(vehiculoActualizado, actualizado.getVehiculo());
        assertEquals(conductorActualizado, actualizado.getConductor());

        verify(ticketValidator).validarConductorEnCurso("87654321", EstadoTicket.EN_CURSO);
        verify(ticketValidator).validarVehiculoEnCurso("XYZ999", EstadoTicket.EN_CURSO);
    }

    @Test
    void actualizarDesdeDTO_conMismoDniYPatente_noDeberiaRevalidar() {
        Ticket ticketExistente = Ticket.builder()
                .id(1L)
                .conductor(conductorExistente)
                .vehiculo(vehiculoExistente)
                .fechaHoraEntrada(LocalDateTime.now())
                .estadoTicket(EstadoTicket.EN_CURSO)
                .build();

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setConductor(conductorRequest);
        dto.setVehiculo(vehiculoRequest);

        ConductorResponseDTO conductorResponse = new ConductorResponseDTO(1L, "Juan", "Pérez", "12345678");
        VehiculoResponseDTO vehiculoResponse = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "ABC123");

        when(conductorService.getEntityById(1L)).thenReturn(conductorExistente);
        when(vehiculoService.getEntityById(1L)).thenReturn(vehiculoExistente);
        when(conductorService.editarConductor(1L, conductorRequest)).thenReturn(conductorResponse);
        when(vehiculoService.editarVehiculo(1L, vehiculoRequest)).thenReturn(vehiculoResponse);
        when(conductorMapper.toEntity(conductorResponse)).thenReturn(conductorExistente);
        when(vehiculoMapper.toEntity(vehiculoResponse)).thenReturn(vehiculoExistente);

        Ticket actualizado = ticketFactory.actualizarDesdeDTO(ticketExistente, dto);

        assertEquals(conductorExistente, actualizado.getConductor());
        assertEquals(vehiculoExistente, actualizado.getVehiculo());
        verify(ticketValidator, never()).validarConductorEnCurso(anyString(), any());
        verify(ticketValidator, never()).validarVehiculoEnCurso(anyString(), any());
    }
}
