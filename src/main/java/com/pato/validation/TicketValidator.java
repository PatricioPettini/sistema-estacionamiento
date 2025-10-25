package com.pato.validation;

import com.pato.model.enums.EstadoTicket;
import com.pato.repository.ITicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketValidator {

    private final ITicketRepository ticketRepository;

    public void validarVehiculoEnCurso(Long idVehiculo, EstadoTicket estadoTicket) {
        ticketRepository.findByVehiculoIdAndEstadoTicket(idVehiculo, estadoTicket)
                .ifPresent(ticket -> {
                    log.warn("Validación fallida: el vehículo con ID {} ya tiene un ticket en curso (ID del ticket existente: {}).",
                            idVehiculo, ticket.getId());
                    throw new IllegalStateException("Ya existe un ticket en curso para este vehículo.");
                });

        log.debug("Validación OK: el vehículo {} no tiene tickets en curso.", idVehiculo);
    }

    public void validarTicketFinalizado(Long idTicket, EstadoTicket estadoTicket) {
        if (estadoTicket == EstadoTicket.FINALIZADO) {
            log.warn("El ticket ya se encuentra finalizado. ID: {}", idTicket);
            throw new IllegalStateException("El ticket ya está finalizado.");
        }

        log.debug("Validación OK: el ticket {} no estába aun finalizado.", idTicket);
    }
}
