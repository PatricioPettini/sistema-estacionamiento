package com.pato.model;

import com.pato.model.enums.TipoVehiculo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TipoVehiculo tipo;

    @Column(unique = true, nullable = true)
    private String patente;
}