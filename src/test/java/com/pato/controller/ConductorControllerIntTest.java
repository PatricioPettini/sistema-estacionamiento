package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.ConductorRequestDTO;
import com.pato.model.Conductor;
import com.pato.repository.IConductorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConductorControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IConductorRepository conductorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        conductorRepository.deleteAll();
        conductorRepository.saveAll(List.of(
                new Conductor(null, "Juan", "Perez", "11111111"),
                new Conductor(null, "Ana", "Lopez", "22222222")
        ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe listar todos los conductores en BD real")
    void getAllConductores_DeberiaRetornarDesdeBD() throws Exception {
        mockMvc.perform(get("/conductor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].dni").value("11111111"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe editar conductor y persistir cambios en BD")
    void editarConductor_DeberiaActualizarEnBD() throws Exception {
        var existente = conductorRepository.findAll().get(0);

        var dto = new ConductorRequestDTO("Carlos", "Gómez", "99999999");

        mockMvc.perform(put("/conductor/editar/" + existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("99999999"));

        var actualizado = conductorRepository.findById(existente.getId()).get();
        assertThat(actualizado.getNombre()).isEqualTo("Carlos");
    }
}
