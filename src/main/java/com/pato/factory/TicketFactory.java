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
import com.pato.model.enums.TipoVehiculo;
import com.pato.service.interfaces.IConductorService;
import com.pato.service.interfaces.IVehiculoService;
import com.pato.validation.TicketValidator;
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
    private final TicketValidator ticketValidator;

    public Ticket crearTicket(TicketRequestDTO ticketRequestDTO){

        String dni=ticketRequestDTO.getConductor().getDni();
        String patente=ticketRequestDTO.getVehiculo().getPatente();

        Conductor conductor=conductorService.getEntityByDni(dni)
                .orElseGet(()->conductorService.crearConductor(ticketRequestDTO.getConductor()));

        ticketValidator.validarConductorEnCurso(conductor.getDni(), EstadoTicket.EN_CURSO);

        Vehiculo vehiculo= vehiculoService.getEntityByPatente(patente)
                .orElseGet(()->vehiculoService.crearVehiculo(ticketRequestDTO.getVehiculo()));

        if(!vehiculo.getTipo().equals(TipoVehiculo.BICICLETA)) {
            ticketValidator.validarVehiculoEnCurso(vehiculo.getPatente(), EstadoTicket.EN_CURSO);
        }

        return Ticket.builder()
                        .conductor(conductor)
                        .vehiculo(vehiculo)
                        .fechaHoraEntrada(LocalDateTime.now())
                        .estadoTicket(EstadoTicket.EN_CURSO)
                        .build();
    }

    public Ticket actualizarDesdeDTO(Ticket ticketExistente, TicketRequestDTO dto) {
        String dniExistente=ticketExistente.getConductor().getDni();
        String patenteExistente=ticketExistente.getVehiculo().getPatente();
        String dniNuevo=dto.getConductor().getDni();
        String patenteNueva=dto.getVehiculo().getPatente();

        if(!dniExistente.equals(dniNuevo)) {
            ticketValidator.validarConductorEnCurso(dto.getConductor().getDni(), EstadoTicket.EN_CURSO);
        }

        Conductor conductor = conductorService
                .getEntityById(ticketExistente.getConductor().getId());
        ConductorResponseDTO conductorActualizado=conductorService.editarConductor(conductor.getId(), dto.getConductor());

        if(!patenteExistente.equals(patenteNueva)) {
            ticketValidator.validarVehiculoEnCurso(dto.getVehiculo().getPatente(), EstadoTicket.EN_CURSO);
        }

        Vehiculo vehiculo = vehiculoService
                .getEntityById(ticketExistente.getVehiculo().getId());
        VehiculoResponseDTO vehiculoActualizado=vehiculoService.editarVehiculo(vehiculo.getId(), dto.getVehiculo());

        return Ticket.builder()
                .id(ticketExistente.getId())
                .fechaHoraEntrada(ticketExistente.getFechaHoraEntrada())
                .estadoTicket(ticketExistente.getEstadoTicket())
                .vehiculo(vehiculoMapper.toEntity(vehiculoActualizado))
                .conductor(conductorMapper.toEntity(conductorActualizado))
                .build();
    }

}
