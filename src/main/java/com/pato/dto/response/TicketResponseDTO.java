package com.pato.dto.response;

import com.pato.model.enums.EstadoTicket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {

    private Long id;

    private VehiculoResponseDTO vehiculo;

    private ConductorResponseDTO conductor;

    private LocalDateTime fechaHoraEntrada;

    private LocalDateTime fechaHoraSalida;

    private EstadoTicket estadoTicket;

    private String observaciones;
}
