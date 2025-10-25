package com.pato.helpers.mappers;

import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.Conductor;
import com.pato.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final ModelMapper modelMapper;

    public TicketResponseDTO toResponseDto(Ticket ticket) {
        TicketResponseDTO dto = modelMapper.map(ticket, TicketResponseDTO.class);
        dto.setVehiculo(modelMapper.map(ticket.getVehiculo(), VehiculoResponseDTO.class));
        dto.setConductor(modelMapper.map(ticket.getConductor(), ConductorResponseDTO.class));
        return dto;
    }

}
