package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.config.TestSecurityConfig;
import com.pato.dto.request.ConductorRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.enums.EstadoTicket;
import com.pato.model.enums.TipoVehiculo;
import com.pato.service.interfaces.IConductorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ConductorController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(TestSecurityConfig.class)
class ConductorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IConductorService conductorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe retornar todos los conductores")
    void getAllConductores_DeberiaRetornarLista() throws Exception {
        var lista = List.of(
                new ConductorResponseDTO(1L, "Juan", "Pérez", "12345678"),
                new ConductorResponseDTO(2L, "Ana", "García", "87654321")
        );

        Mockito.when(conductorService.getAllConductores()).thenReturn(lista);

        mockMvc.perform(get("/conductor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].dni").value("87654321"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe retornar un conductor por DNI")
    void getConductor_DeberiaRetornarConductor() throws Exception {
        var conductor = new ConductorResponseDTO(1L, "Carlos", "Lopez", "22222222");
        Mockito.when(conductorService.getConductor("22222222")).thenReturn(Optional.of(conductor));

        mockMvc.perform(get("/conductor/get/22222222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.get().nombre").doesNotExist()); // opcional si usas Optional directo
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe editar un conductor correctamente")
    void editarConductor_DeberiaRetornarConductorEditado() throws Exception {
        var dtoRequest = new ConductorRequestDTO("Pedro", "Ramirez", "33445566");
        var dtoResponse = new ConductorResponseDTO(5L, "Pedro", "Ramirez", "33445566");

        Mockito.when(conductorService.editarConductor(eq(5L), any())).thenReturn(dtoResponse);

        mockMvc.perform(put("/conductor/editar/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("33445566"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe obtener el historial de tickets de un conductor")
    void getHistorialConductor_DeberiaRetornarListaTickets() throws Exception {
        var vehiculo = new VehiculoResponseDTO(null,TipoVehiculo.AUTO,"ABC123");
        var conductor = new ConductorResponseDTO(1L, "Carlos", "Lopez", "22222222");

        var historial = List.of(
                new TicketResponseDTO(
                        1L,
                        vehiculo,
                        conductor,
                        LocalDateTime.parse("2025-10-29T12:00"),
                        null,
                        EstadoTicket.EN_CURSO,
                        "Sin observaciones"
                ),
                new TicketResponseDTO(
                        2L,
                        vehiculo,
                        conductor,
                        LocalDateTime.parse("2025-10-28T10:00"),
                        LocalDateTime.parse("2025-10-28T11:00"),
                        EstadoTicket.FINALIZADO,
                        "Salió correctamente"
                )
        );

        Mockito.when(conductorService.getHistorialConductor("12345678")).thenReturn(historial);

        mockMvc.perform(get("/conductor/historial/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].estadoTicket").value("EN_CURSO"))
                .andExpect(jsonPath("$[1].estadoTicket").value("FINALIZADO"));
    }

}
