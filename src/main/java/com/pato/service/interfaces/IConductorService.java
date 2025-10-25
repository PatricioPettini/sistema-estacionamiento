package com.pato.service.interfaces;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.model.Conductor;

import java.util.List;
import java.util.Optional;

public interface IConductorService {
    List<ConductorResponseDTO> getAllConductores();
    Optional<ConductorResponseDTO> getConductor(String dni);
    ConductorResponseDTO editarConductor(Long idConductor, ConductorRequestDTO conductorRequestDTO);
    List<TicketResponseDTO> getHistorialConductor(String dni);
    Optional<Conductor> getEntityByDni(String dni);
    Conductor getEntityById(Long idConductor);
    Conductor crearConductor(ConductorRequestDTO conductorRequestDTO);
}
