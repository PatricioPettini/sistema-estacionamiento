package com.pato.model;

import com.pato.model.enums.EstadoTicket;
import com.pato.validation.HorarioValido;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "conductor_id", nullable = false)
    private Conductor conductor;

    @HorarioValido
    @PastOrPresent
    private LocalDateTime fechaHoraEntrada;

    @HorarioValido
    private LocalDateTime fechaHoraSalida;

    @Enumerated(EnumType.STRING)
    private EstadoTicket estadoTicket;
}