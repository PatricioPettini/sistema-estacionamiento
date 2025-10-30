package com.pato.controller;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.service.interfaces.IConductorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conductor")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ConductorController {

    private final IConductorService conductorService;

    @GetMapping
    public ResponseEntity<List<ConductorResponseDTO>> getAllConductores() {
        return ResponseEntity.ok(conductorService.getAllConductores());
    }

    @GetMapping("/get/{dni}")
    public ResponseEntity<Optional<ConductorResponseDTO>> getConductor(@PathVariable String dni) {
        return ResponseEntity.ok(conductorService.getConductor(dni));
    }

    @PutMapping("/editar/{idConductor}")
    public ResponseEntity<ConductorResponseDTO> editarConductor(@PathVariable Long idConductor,@Valid @RequestBody ConductorRequestDTO conductorRequestDTO) {
        return ResponseEntity.ok(conductorService.editarConductor(idConductor, conductorRequestDTO));
    }

    @GetMapping("/historial/{dni}")
    public ResponseEntity<List<TicketResponseDTO>> getHistorialConductor(@PathVariable String dni) {
        return ResponseEntity.ok(conductorService.getHistorialConductor(dni));
    }
}
