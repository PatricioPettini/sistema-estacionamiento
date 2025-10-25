package com.pato.dto.response;

import com.pato.model.enums.TipoVehiculo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoResponseDTO {
    private Long id;

    private TipoVehiculo tipo;

    private String patente;
}
