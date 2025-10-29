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

    public void validarTicketFinalizado(Long idTicket, EstadoTicket estadoTicket) {
        if (estadoTicket == EstadoTicket.FINALIZADO) {
            log.warn("El ticket ya se encuentra finalizado. ID: {}", idTicket);
            throw new IllegalStateException("El ticket ya está finalizado.");
        }

        log.debug("Validación OK: el ticket {} no estába aun finalizado.", idTicket);
    }

    public void validarVehiculoEnCurso(String patente, EstadoTicket estadoTicket){
        if(ticketRepository.existsByVehiculoPatenteAndEstadoTicket(patente, estadoTicket)){
            throw new IllegalArgumentException("Ya existe un ticket en curso para este vehículo.");
        }
    }

    public void validarConductorEnCurso(String dni, EstadoTicket estadoTicket){
        if(ticketRepository.existsByConductorDniAndEstadoTicket(dni, estadoTicket)){
            throw new IllegalArgumentException("Ya existe un ticket en curso para este conductor.");
        }
    }
}