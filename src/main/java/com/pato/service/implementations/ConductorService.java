package com.pato.service.implementations;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.factory.ConductorFactory;
import com.pato.helpers.mappers.ConductorMapper;
import com.pato.helpers.mappers.TicketMapper;
import com.pato.model.Conductor;
import com.pato.repository.IConductorRepository;
import com.pato.repository.ITicketRepository;
import com.pato.service.interfaces.IConductorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConductorService implements IConductorService {

    private final IConductorRepository conductorRepository;
    private final ITicketRepository ticketRepository;
    private final ConductorMapper conductorMapper;
    private final TicketMapper ticketMapper;
    private final ConductorFactory conductorFactory;


    @Override
    public List<ConductorResponseDTO> getAllConductores() {
        log.info("Obteniendo todos los conductores registrados...");
        List<ConductorResponseDTO> lista = conductorRepository.findAll().stream()
                .map(conductorMapper::toResponseDto)
                .toList();
        log.info("Se encontraron {} conductores en la base de datos.", lista.size());
        return lista;
    }

    @Override
    public Optional<ConductorResponseDTO> getConductor(String dni) {
        log.debug("Buscando conductor con DNI: {}", dni);
        Optional<ConductorResponseDTO> conductor = getEntityByDni(dni)
                .map(conductorMapper::toResponseDto);

        if (conductor.isPresent()) {
            log.info("Conductor encontrado con DNI: {}", dni);
        } else {
            log.warn("No se encontró conductor con DNI: {}", dni);
        }

        return conductor;
    }

    @Override
    public ConductorResponseDTO editarConductor(Long idConductor, ConductorRequestDTO conductorRequestDTO) {
        log.info("Editando conductor con ID: {}", idConductor);

        Conductor conductor=getEntityById(idConductor);

        Conductor actualizado=conductorMapper.toEntity(conductorRequestDTO);
        actualizado.setId(conductor.getId());

        Conductor guardado=conductorRepository.save(actualizado);

        log.info("Conductor con ID {} actualizado correctamente.", idConductor);
        return conductorMapper.toResponseDto(guardado);
    }

    @Override
    public List<TicketResponseDTO> getHistorialConductor(String dni) {
        log.info("Consultando historial de tickets para el conductor con DNI: {}", dni);
        List<TicketResponseDTO> historial = ticketRepository.findByConductorDni(dni)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();

        log.debug("Historial obtenido: {} tickets encontrados.", historial.size());
        return historial;
    }

    @Override
    public Optional<Conductor> getEntityByDni(String dni) {
        return conductorRepository.findByDni(dni);
    }

    @Override
    public Conductor getEntityById(Long idConductor) {
        return conductorRepository.findById(idConductor)
                .orElseThrow(() -> {
                    log.error("Error: no existe conductor con ID {}", idConductor);
                    return new IllegalArgumentException("El conductor con ese ID no existe");
                });    }

    @Override
    public Conductor crearConductor(ConductorRequestDTO conductorRequestDTO) {
        log.info("Creando nuevo conductor con DNI: {}", conductorRequestDTO.getDni());

        Conductor nuevoConductor= conductorFactory.crearConductor(conductorRequestDTO);
        Conductor guardado = conductorRepository.save(nuevoConductor);

        log.info("Conductor creado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }
}