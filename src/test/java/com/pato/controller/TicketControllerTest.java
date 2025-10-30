package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.ObservacionRequest;
import com.pato.dto.request.TicketRequestDTO;
import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.model.enums.TipoVehiculo;
import com.pato.service.interfaces.ITicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /ticket/get/{id} debe retornar un ticket")
    void getTicket_deberiaRetornarTicket() throws Exception {
        TicketResponseDTO response = new TicketResponseDTO();
        response.setId(1L);

        Mockito.when(ticketService.getTicket(1L)).thenReturn(response);

        mockMvc.perform(get("/ticket/get/{idTicket}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /ticket debe retornar lista de tickets")
    void getAllTickets_deberiaRetornarLista() throws Exception {
        TicketResponseDTO t1 = new TicketResponseDTO();
        t1.setId(1L);
        TicketResponseDTO t2 = new TicketResponseDTO();
        t2.setId(2L);

        Mockito.when(ticketService.getAllTickets()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/ticket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("POST /ticket/crear debe crear un ticket y retornar DTO")
    void crearTicket_deberiaGuardarYRetornarDTO() throws Exception {
        TicketRequestDTO request = new TicketRequestDTO();
        request.setConductor(new ConductorRequestDTO("Juan", "Pérez", "12345678"));
        request.setVehiculo(new VehiculoRequestDTO(TipoVehiculo.AUTO, "ABC123"));

        TicketResponseDTO response = new TicketResponseDTO();
        response.setId(1L);

        Mockito.when(ticketService.crearTicket(any(TicketRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/ticket/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /ticket/editar/{id} debe actualizar un ticket existente")
    void editarTicket_deberiaActualizarYRetornarDTO() throws Exception {
        TicketRequestDTO request = new TicketRequestDTO();
        request.setConductor(new ConductorRequestDTO("María", "López", "87654321"));
        request.setVehiculo(new VehiculoRequestDTO(TipoVehiculo.MOTO, "XYZ789"));

        TicketResponseDTO response = new TicketResponseDTO();
        response.setId(99L);

        Mockito.when(ticketService.editarTicket(eq(99L), any(TicketRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/ticket/editar/{idTicket}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99L));
    }

    @Test
    @DisplayName("PATCH /ticket/finalizar/{id} debe marcar ticket como finalizado")
    void finalizarTicket_deberiaFinalizarYRetornarDTO() throws Exception {
        ObservacionRequest request = new ObservacionRequest();
        request.setObservaciones("Vehículo retirado correctamente");

        TicketResponseDTO response = new TicketResponseDTO();
        response.setId(5L);

        Mockito.when(ticketService.salidaVehiculo(eq(5L), eq("Vehículo retirado correctamente")))
                .thenReturn(response);

        mockMvc.perform(patch("/ticket/finalizar/{idTicket}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }
}
