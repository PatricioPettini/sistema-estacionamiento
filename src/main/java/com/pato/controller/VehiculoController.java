package com.pato.controller;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.service.interfaces.IVehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehiculo")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class VehiculoController {

    private final IVehiculoService vehiculoService;

    @GetMapping
    public ResponseEntity<List<VehiculoResponseDTO>> getAllVehiculos() {
        return ResponseEntity.ok(vehiculoService.getAllVehiculos());
    }

    @GetMapping("/get/{patente}")
    public ResponseEntity<Optional<VehiculoResponseDTO>> getVehiculo(@PathVariable String patente) {
        return ResponseEntity.ok(vehiculoService.getVehiculo(patente));
    }

    @PutMapping("/editar/{idVehiculo}")
    public ResponseEntity<VehiculoResponseDTO> editarVehiculo(@PathVariable Long idVehiculo,@Valid @RequestBody VehiculoRequestDTO vehiculoRequestDTO) {
        return ResponseEntity.ok(vehiculoService.editarVehiculo(idVehiculo, vehiculoRequestDTO));
    }

    @GetMapping("/historial/{patente}")
    public ResponseEntity<List<TicketResponseDTO>> getHistorialVehiculo(@PathVariable String patente) {
        return ResponseEntity.ok(vehiculoService.getHistorialVehiculo(patente));
    }
}
