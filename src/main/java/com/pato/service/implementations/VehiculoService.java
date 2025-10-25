package com.pato.service.implementations;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.factory.VehiculoFactory;
import com.pato.helpers.mappers.TicketMapper;
import com.pato.helpers.mappers.VehiculoMapper;
import com.pato.model.Vehiculo;
import com.pato.repository.ITicketRepository;
import com.pato.repository.IVehiculoRepository;
import com.pato.service.interfaces.IVehiculoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculoService implements IVehiculoService {

    private final IVehiculoRepository vehiculoRepository;
    private final VehiculoMapper vehiculoMapper;
    private final ITicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final VehiculoFactory vehiculoFactory;

    @Override
    public List<VehiculoResponseDTO> getAllVehiculos() {
        log.info("Obteniendo todos los vehiculos registrados...");

        List<VehiculoResponseDTO> lista = vehiculoRepository.findAll().stream()
                .map(vehiculoMapper::toResponseDto)
                .toList();

        log.info("Se encontraron {} vehiculos en la base de datos.", lista.size());
        return lista;
    }

    @Override
    public Optional<VehiculoResponseDTO> getVehiculo(String patente) {
        log.debug("Buscando vehiculo con patente: {}", patente);

        Optional<VehiculoResponseDTO> vehiculo =  getEntityByPatente(patente)
                .map(vehiculoMapper::toResponseDto);

        if (vehiculo.isPresent()) {
            log.info("Conductor encontrado con DNI: {}", patente);
        } else {
            log.warn("No se encontró conductor con DNI: {}", patente);
        }

        return vehiculo;
    }

    @Override
    public VehiculoResponseDTO editarVehiculo(Long idVehiculo, VehiculoRequestDTO vehiculoRequestDTO) {
        log.info("Editando vehiculo con ID: {}", idVehiculo);

        Vehiculo vehiculo=getEntityById(idVehiculo);

        Vehiculo actualizado=vehiculoMapper.toEntity(vehiculoRequestDTO);
        actualizado.setId(vehiculo.getId());

        Vehiculo guardado=vehiculoRepository.save(actualizado);

        log.info("Vehiculo con ID {} actualizado correctamente.", idVehiculo);
        return vehiculoMapper.toResponseDto(guardado);
    }

    @Override
    public List<TicketResponseDTO> getHistorialVehiculo(String patente) {
        log.info("Consultando historial de tickets para el conductor con DNI: {}", patente);

        List<TicketResponseDTO> historial= ticketRepository.findByVehiculoPatente(patente)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();

        log.debug("Historial obtenido: {} tickets encontrados.", historial.size());
        return historial;
    }

    @Override
    public Optional<Vehiculo> getEntityByPatente(String patente) {
        return vehiculoRepository.findByPatente(patente);
    }

    @Override
    public Vehiculo getEntityById(Long idVehiculo) {
        return vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> {
                    log.error("Error: no existe vehiculo con ID {}", idVehiculo);
                    return new IllegalArgumentException("El vehiculo con ese ID no existe");
                });    }

    @Override
    public Vehiculo crearVehiculo(VehiculoRequestDTO vehiculoRequestDTO) {
        log.info("Creando nuevo vehiculo con patente: {}", vehiculoRequestDTO.getPatente());

        Vehiculo nuevoVehiculo= vehiculoFactory.crearVehiculo(vehiculoRequestDTO);
        Vehiculo guardado=vehiculoRepository.save(nuevoVehiculo);

        log.info("Vehiculo creado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }
}