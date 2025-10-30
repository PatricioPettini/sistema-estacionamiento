package com.pato.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {

    @NotNull(message = "El vehículo es obligatorio")
    @Valid
    private VehiculoRequestDTO vehiculo;

    @NotNull(message = "El conductor es obligatorio")
    @Valid
    private ConductorRequestDTO conductor;
}
