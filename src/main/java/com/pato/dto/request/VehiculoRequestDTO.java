package com.pato.dto.request;

import com.pato.model.enums.TipoVehiculo;
import com.pato.validation.PatenteSegunTipo;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PatenteSegunTipo
public class VehiculoRequestDTO {

    @NotNull(message = "El tipo de vehículo es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipo;

    private String patente;

}