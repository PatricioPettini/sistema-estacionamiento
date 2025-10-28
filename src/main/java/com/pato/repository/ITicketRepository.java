package com.pato.repository;

import com.pato.model.Ticket;
import com.pato.model.enums.EstadoTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByConductorDni(String dni);
    List<Ticket> findByVehiculoPatente(String patente);
    Optional<Ticket> findByVehiculoIdAndEstadoTicket(Long idVehiculo, EstadoTicket estadoTicket);
    boolean existsByVehiculoPatenteAndEstadoTicket(String patente, EstadoTicket estadoTicket);
    boolean existsByConductorDniAndEstadoTicket(String dni, EstadoTicket estadoTicket);
    void deleteByFechaBefore(LocalDateTime fecha);
}