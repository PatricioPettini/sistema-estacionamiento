package com.pato.service.interfaces;

import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.model.Ticket;

import java.util.List;

public interface ITicketService {
    TicketResponseDTO getTicket(Long idTicket);
    TicketResponseDTO editarTicket(Long idTicket, TicketRequestDTO ticketRequestDTO);
    List<TicketResponseDTO> getAllTickets();
    TicketResponseDTO crearTicket(TicketRequestDTO ticketRequestDTO);
    TicketResponseDTO salidaVehiculo(Long idTicket);
    Ticket getEntityById(Long idTicket);
}
