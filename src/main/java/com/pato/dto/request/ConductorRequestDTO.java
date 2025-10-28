package com.pato.dto.request;

import com.pato.validation.DatosConductor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DatosConductor
public class ConductorRequestDTO {
//    @NotBlank(message = "El nombre es obligatorio")
//    @Pattern(
//            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?: [A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$",
//            message = "El nombre solo puede contener letras y espacios"
//    )
    private String nombre;

//    @NotBlank(message = "El apellido es obligatorio")
//    @Pattern(
//            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?: [A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$",
//            message = "El apellido solo puede contener letras y espacios"
//    )
    private String apellido;


//    @NotBlank(message = "El teléfono es obligatorio")
//    private String telefono;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{7,8}", message = "El DNI debe tener entre 7 y 8 dígitos numéricos")
    private String dni;
}