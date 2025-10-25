package com.pato.helpers.mappers;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.model.Conductor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorMapper {

    private final ModelMapper modelMapper;

    public ConductorResponseDTO toResponseDto(Conductor conductor){
        return modelMapper.map(conductor,ConductorResponseDTO.class);
    }

    public Conductor toEntity(ConductorRequestDTO dto){
        return modelMapper.map(dto,Conductor.class);
    }

    public Conductor toEntity(ConductorResponseDTO dto){
        return modelMapper.map(dto,Conductor.class);
    }
}
