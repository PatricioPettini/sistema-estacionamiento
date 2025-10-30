package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.enums.EstadoTicket;
import com.pato.model.enums.TipoVehiculo;
import com.pato.service.interfaces.IConductorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConductorController.class)
class ConductorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IConductorService conductorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllConductores_deberiaRetornarListaDeConductores() throws Exception {
        var conductor1 = new ConductorResponseDTO(1L, "Juan", "Pérez", "12345678");
        var conductor2 = new ConductorResponseDTO(2L, "Ana", "García", "87654321");

        Mockito.when(conductorService.getAllConductores())
                .thenReturn(List.of(conductor1, conductor2));

        mockMvc.perform(get("/conductor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan")))
                .andExpect(jsonPath("$[1].dni", is("87654321")));
    }

    @Test
    void getConductor_porDni_deberiaRetornarConductor() throws Exception {
        var response = new ConductorResponseDTO(1L, "Juan", "Pérez", "12345678");
        Mockito.when(conductorService.getConductor("12345678"))
                .thenReturn(Optional.of(response));

        mockMvc.perform(get("/conductor/get/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")));
    }

    @Test
    void editarConductor_deberiaActualizarYRetornarConductorEditado() throws Exception {
        var request = new ConductorRequestDTO("Carlos", "López", "87654321");
        var response = new ConductorResponseDTO(1L, "Carlos", "López", "87654321");

        Mockito.when(conductorService.editarConductor(Mockito.eq(1L), Mockito.any(ConductorRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/conductor/editar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos")))
                .andExpect(jsonPath("$.apellido", is("López")))
                .andExpect(jsonPath("$.dni", is("87654321")));
    }

    @Test
    void getHistorialConductor_deberiaRetornarListaDeTickets() throws Exception {
        var vehiculo = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO , "ABC123");
        var conductor = new ConductorResponseDTO(1L, "Juan", "Pérez", "12345678");

        var ticket = new TicketResponseDTO(
                1L,
                vehiculo,
                conductor,
                LocalDateTime.of(2025, 10, 29, 10, 0),
                LocalDateTime.of(2025, 10, 29, 12, 0),
                EstadoTicket.EN_CURSO,
                "Sin observaciones"
        );

        Mockito.when(conductorService.getHistorialConductor("12345678"))
                .thenReturn(List.of(ticket));

        mockMvc.perform(get("/conductor/historial/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vehiculo.patente", is("ABC123")))
                .andExpect(jsonPath("$[0].estadoTicket", is("EN_CURSO")))
                .andExpect(jsonPath("$[0].conductor.nombre", is("Juan")));
    }
}
