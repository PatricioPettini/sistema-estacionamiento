package com.pato.helpers.mappers;

import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.Vehiculo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehiculoMapper {

    private final ModelMapper modelMapper;

    public VehiculoResponseDTO toResponseDto(Vehiculo vehiculo){
        return modelMapper.map(vehiculo,VehiculoResponseDTO.class);
    }

    public Vehiculo toEntity(VehiculoRequestDTO dto){
        Vehiculo vehiculo= new Vehiculo();
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setPatente(dto.getPatente().toUpperCase());
        return vehiculo;
    }

    public Vehiculo toEntity(VehiculoResponseDTO dto){
        return modelMapper.map(dto,Vehiculo.class);
    }
}
