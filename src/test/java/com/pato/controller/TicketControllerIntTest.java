package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.request.ObservacionRequest;
import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.model.enums.TipoVehiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TicketControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear y luego obtener un ticket")
    void crearYObtenerTicket() throws Exception {
        TicketRequestDTO request = new TicketRequestDTO();
        request.setConductor(new ConductorRequestDTO("Carlos", "Gómez", "11112222")); // ✅ dni, nombre, apellido
        request.setVehiculo(new VehiculoRequestDTO(TipoVehiculo.AUTO, "AAA111"));

        // Crear
        String response = mockMvc.perform(post("/ticket/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long idCreado = objectMapper.readTree(response).get("id").asLong();

        // Obtener
        mockMvc.perform(get("/ticket/get/{id}", idCreado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idCreado));
    }

    @Test
    @DisplayName("Debe finalizar un ticket existente")
    void finalizarTicket() throws Exception {
        TicketRequestDTO request = new TicketRequestDTO();
        request.setConductor(new ConductorRequestDTO("Lucía", "Martínez", "22223333")); // ✅ correcto
        request.setVehiculo(new VehiculoRequestDTO(TipoVehiculo.MOTO, "BBB222"));

        // Crear
        String response = mockMvc.perform(post("/ticket/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // Finalizar
        ObservacionRequest obs = new ObservacionRequest();
        obs.setObservaciones("Salida sin incidentes");

        mockMvc.perform(patch("/ticket/finalizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(obs)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }
}
