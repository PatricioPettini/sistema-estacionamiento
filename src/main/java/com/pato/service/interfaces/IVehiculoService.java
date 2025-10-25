package com.pato.service.interfaces;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.Vehiculo;

import java.util.List;
import java.util.Optional;

public interface IVehiculoService {
    List<VehiculoResponseDTO> getAllVehiculos();
    Optional<VehiculoResponseDTO> getVehiculo(String patente);
    VehiculoResponseDTO editarVehiculo(Long idVehiculo, VehiculoRequestDTO vehiculoRequestDTO);
    List<TicketResponseDTO> getHistorialVehiculo(String idVehiculo);
    Optional<Vehiculo> getEntityByPatente(String patente);
    Vehiculo getEntityById(Long idVehiculo);
    Vehiculo crearVehiculo(VehiculoRequestDTO vehiculoRequestDTO);
}
