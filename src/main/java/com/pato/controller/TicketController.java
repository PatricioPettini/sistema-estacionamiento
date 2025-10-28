package com.pato.controller;

import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.service.interfaces.ITicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final ITicketService ticketService;

    @GetMapping("/get/{idTicket}")
    public ResponseEntity<TicketResponseDTO> getTicket(@PathVariable Long idTicket){
        return ResponseEntity.ok(ticketService.getTicket(idTicket));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PostMapping("/crear")
    public ResponseEntity<TicketResponseDTO> crearTicket(@Valid @RequestBody TicketRequestDTO ticketRequestDTO){
        return ResponseEntity.ok(ticketService.crearTicket(ticketRequestDTO));
    }

    @PutMapping("/editar/{idTicket}")
    public ResponseEntity<TicketResponseDTO> editar(@PathVariable Long idTicket,@Valid @RequestBody TicketRequestDTO ticketRequestDTO){
        return ResponseEntity.ok(ticketService.editarTicket(idTicket, ticketRequestDTO));
    }

    @PatchMapping("/finalizar/{idTicket}")
    public ResponseEntity<TicketResponseDTO> finalizarTicket(@PathVariable Long idTicket, @RequestBody String observaciones){
        return ResponseEntity.ok(ticketService.salidaVehiculo(idTicket, observaciones));
    }
}