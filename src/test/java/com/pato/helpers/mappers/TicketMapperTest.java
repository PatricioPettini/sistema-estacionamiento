package com.pato.helpers.mappers;

import com.pato.dto.response.TicketResponseDTO;
import com.pato.model.Conductor;
import com.pato.model.Ticket;
import com.pato.model.Vehiculo;
import com.pato.model.enums.EstadoTicket;
import com.pato.model.enums.TipoVehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketMapperTest {

    private TicketMapper ticketMapper;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        ticketMapper = new TicketMapper(modelMapper);
    }

    @Test
    void toResponseDto_deberiaMapearVehiculoYConductorCorrectamente() {
        Conductor conductor = new Conductor(1L, "Juan", "Pérez", "12345678");
        Vehiculo vehiculo = new Vehiculo(1L, TipoVehiculo.AUTO, "ABC123");
        Ticket ticket = new Ticket(1L, vehiculo, conductor, LocalDateTime.now(), null, EstadoTicket.EN_CURSO, "Sin observaciones");

        TicketResponseDTO dto = ticketMapper.toResponseDto(ticket);

        assertNotNull(dto);
        assertEquals(ticket.getId(), dto.getId());
        assertEquals(ticket.getEstadoTicket(), dto.getEstadoTicket());
        assertNotNull(dto.getVehiculo());
        assertNotNull(dto.getConductor());

        assertEquals(ticket.getVehiculo().getPatente(), dto.getVehiculo().getPatente());
        assertEquals(ticket.getConductor().getDni(), dto.getConductor().getDni());
    }
}
