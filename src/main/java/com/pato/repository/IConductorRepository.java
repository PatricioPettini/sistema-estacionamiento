package com.pato.repository;

import com.pato.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IConductorRepository extends JpaRepository<Conductor, Long> {
    Optional<Conductor> findByDni(String dni);
}
