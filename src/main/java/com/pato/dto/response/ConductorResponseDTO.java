package com.pato.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConductorResponseDTO {
    private Long id;

    private String nombre;

    private String apellido;

//    private String telefono;

    private String dni;
}
