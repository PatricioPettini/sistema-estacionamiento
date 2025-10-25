package com.pato.factory;

import com.pato.dto.request.ConductorRequestDTO;
import com.pato.model.Conductor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConductorFactory {

    private final ModelMapper modelMapper;

    public Conductor crearConductor(ConductorRequestDTO dto){
        return modelMapper.map(dto, Conductor.class);
    }
}