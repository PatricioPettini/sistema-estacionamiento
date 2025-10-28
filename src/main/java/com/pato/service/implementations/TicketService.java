package com.pato.service.implementations;

import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.factory.TicketFactory;
import com.pato.helpers.mappers.TicketMapper;
import com.pato.model.Ticket;
import com.pato.model.enums.EstadoTicket;
import com.pato.repository.ITicketRepository;
import com.pato.service.interfaces.ITicketService;
import com.pato.validation.TicketValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService implements ITicketService {

    private final ITicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketFactory ticketFactory;
    private final TicketValidator ticketValidator;


    @Override
    public TicketResponseDTO getTicket(Long idTicket) {
        log.debug("Buscando ticket con ID: {}", idTicket);
        Ticket ticket = getEntityById(idTicket);
        log.info("Ticket obtenido con ID: {}", idTicket);
        return ticketMapper.toResponseDto(ticket);    }

    @Override
    public TicketResponseDTO editarTicket(Long idTicket, TicketRequestDTO dto) {
        log.info("Editando ticket con ID: {}", idTicket);

        Ticket ticketExistente = getEntityById(idTicket);

        ticketValidator.validarTicketFinalizado(idTicket, ticketExistente.getEstadoTicket());

        Ticket actualizado = ticketFactory.actualizarDesdeDTO(ticketExistente, dto);

        Ticket guardado = ticketRepository.save(actualizado);
        log.info("Ticket con ID {} actualizado correctamente.", guardado.getId());


        return ticketMapper.toResponseDto(guardado);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        log.info("Obteniendo listado completo de tickets...");
        List<TicketResponseDTO> lista = ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();

        log.debug("Se encontraron {} tickets registrados.", lista.size());
        return lista;
    }

    @Override
    public TicketResponseDTO crearTicket(TicketRequestDTO ticketRequestDTO) {
        log.info("Creando nuevo ticket para vehículo con patente: {}",
                ticketRequestDTO.getVehiculo().getPatente());

        Ticket nuevoTicket=ticketFactory.crearTicket(ticketRequestDTO);

        ticketValidator.validarVehiculoEnCurso(nuevoTicket.getVehiculo().getId(), nuevoTicket.getEstadoTicket());

        Ticket guardado=ticketRepository.save(nuevoTicket);
        log.info("Ticket creado exitosamente con ID: {} para vehículo: {}",
                guardado.getId(),
                guardado.getVehiculo().getPatente());

        return ticketMapper.toResponseDto(guardado);
    }

    @Override
    public TicketResponseDTO salidaVehiculo(Long idTicket, String observaciones) {
        log.info("Registrando salida para ticket ID: {}", idTicket);

        Ticket ticket=getEntityById(idTicket);

        ticketValidator.validarTicketFinalizado(idTicket, ticket.getEstadoTicket());

        ticket.setFechaHoraSalida(LocalDateTime.now());
        ticket.setEstadoTicket(EstadoTicket.FINALIZADO);

        if(observaciones!=null && !observaciones.isEmpty()){
            ticket.setObservaciones(observaciones);
        }

        Ticket guardado=ticketRepository.save(ticket);
        log.info("Ticket ID {} finalizado correctamente. Hora de salida: {}",
                guardado.getId(),
                guardado.getFechaHoraSalida());

        return ticketMapper.toResponseDto(guardado);
    }

    @Override
    public Ticket getEntityById(Long idTicket) {
        return ticketRepository.findById(idTicket)
                .orElseThrow(() -> {
                    log.error("Error al buscar ticket: no existe ticket con ID {}", idTicket);
                    return new IllegalArgumentException("No existe ticket con ese id!");
                });    }
}