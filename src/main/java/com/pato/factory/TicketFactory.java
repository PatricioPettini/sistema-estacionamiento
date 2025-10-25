package com.pato.factory;

import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.helpers.mappers.ConductorMapper;
import com.pato.helpers.mappers.VehiculoMapper;
import com.pato.model.Conductor;
import com.pato.model.Ticket;
import com.pato.model.Vehiculo;
import com.pato.model.enums.EstadoTicket;
import com.pato.service.interfaces.IConductorService;
import com.pato.service.interfaces.IVehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TicketFactory {

    private final IConductorService conductorService;
    private final IVehiculoService vehiculoService;
    private final ConductorMapper conductorMapper;
    private final VehiculoMapper vehiculoMapper;

    public Ticket crearTicket(TicketRequestDTO ticketRequestDTO){
        Conductor conductor=conductorService.getEntityByDni(ticketRequestDTO.getConductor().getDni())
                .orElseGet(()->conductorService.crearConductor(ticketRequestDTO.getConductor()));

        Vehiculo vehiculo= vehiculoService.getEntityByPatente(ticketRequestDTO.getVehiculo().getPatente())
                .orElseGet(()->vehiculoService.crearVehiculo(ticketRequestDTO.getVehiculo()));

        return Ticket.builder()
                        .conductor(conductor)
                        .vehiculo(vehiculo)
                        .fechaHoraEntrada(LocalDateTime.now())
                        .estadoTicket(EstadoTicket.EN_CURSO)
                        .build();
    }

    public Ticket actualizarDesdeDTO(Ticket ticketExistente, TicketRequestDTO dto) {
        Conductor conductor = conductorService
                .getEntityById(ticketExistente.getConductor().getId());
        ConductorResponseDTO confuctorActualizado=conductorService.editarConductor(conductor.getId(), dto.getConductor());

        Vehiculo vehiculo = vehiculoService
                .getEntityById(ticketExistente.getVehiculo().getId());
        VehiculoResponseDTO vehiculoActualizado=vehiculoService.editarVehiculo(vehiculo.getId(), dto.getVehiculo());

        return Ticket.builder()
                .id(ticketExistente.getId())
                .fechaHoraEntrada(ticketExistente.getFechaHoraEntrada())
                .estadoTicket(ticketExistente.getEstadoTicket())
                .vehiculo(vehiculoMapper.toEntity(vehiculoActualizado))
                .conductor(conductorMapper.toEntity(confuctorActualizado))
                .build();
    }

}
