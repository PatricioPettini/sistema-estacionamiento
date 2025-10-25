package com.pato.repository;

import com.pato.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IVehiculoRepository extends JpaRepository<Vehiculo,Long> {
    Optional<Vehiculo> findByPatente(String patente);
}
