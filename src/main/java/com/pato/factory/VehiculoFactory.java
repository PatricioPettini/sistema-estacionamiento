package com.pato.factory;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.model.Vehiculo;
import com.pato.model.enums.TipoVehiculo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehiculoFactory {

    private final ModelMapper modelMapper;

    public Vehiculo crearVehiculo(VehiculoRequestDTO dto){
        Vehiculo vehiculo=modelMapper.map(dto, Vehiculo.class);
        if(!vehiculo.getTipo().equals(TipoVehiculo.BICICLETA)) {
            vehiculo.setPatente(dto.getPatente().toUpperCase());
        }
        return vehiculo;
    }
}