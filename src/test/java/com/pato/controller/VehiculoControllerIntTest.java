package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.enums.EstadoTicket;
import com.pato.model.enums.TipoVehiculo;
import com.pato.service.interfaces.IVehiculoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VehiculoControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IVehiculoService vehiculoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver todos los vehículos")
    void getAllVehiculos_DeberiaRetornarLista() throws Exception {
        List<VehiculoResponseDTO> lista = List.of(
                new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "AAA111"),
                new VehiculoResponseDTO(2L, TipoVehiculo.MOTO, "BBB222")
        );

        Mockito.when(vehiculoService.getAllVehiculos()).thenReturn(lista);

        mockMvc.perform(get("/vehiculo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].patente", is("AAA111")))
                .andExpect(jsonPath("$[1].tipo", is("MOTO")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver un vehículo por patente")
    void getVehiculo_DeberiaRetornarVehiculo() throws Exception {
        VehiculoResponseDTO vehiculo = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "AAA111");

        Mockito.when(vehiculoService.getVehiculo("AAA111"))
                .thenReturn(Optional.of(vehiculo));

        mockMvc.perform(get("/vehiculo/get/AAA111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.tipo", is("AUTO")))
                .andExpect(jsonPath("$.patente", is("AAA111")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe editar un vehículo correctamente")
    void editarVehiculo_DeberiaActualizarVehiculo() throws Exception {
        VehiculoRequestDTO request = new VehiculoRequestDTO(TipoVehiculo.AUTO, "CCC333");
        VehiculoResponseDTO actualizado = new VehiculoResponseDTO(3L, TipoVehiculo.AUTO, "CCC333");

        Mockito.when(vehiculoService.editarVehiculo(eq(3L), any(VehiculoRequestDTO.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/vehiculo/editar/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patente", is("CCC333")))
                .andExpect(jsonPath("$.tipo", is("AUTO")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Debe devolver el historial de tickets del vehículo")
    void getHistorialVehiculo_DeberiaRetornarListaDeTickets() throws Exception {
        VehiculoResponseDTO vehiculo = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "AAA111");

        List<TicketResponseDTO> historial = List.of(
                new TicketResponseDTO(
                        1L,
                        vehiculo,
                        null, // conductor (puede ser null en este contexto)
                        LocalDateTime.parse("2025-10-28T10:00:00"),
                        LocalDateTime.parse("2025-10-28T12:00:00"),
                        EstadoTicket.EN_CURSO,
                        "Ingreso correcto"
                ),
                new TicketResponseDTO(
                        2L,
                        vehiculo,
                        null,
                        LocalDateTime.parse("2025-10-28T15:00:00"),
                        LocalDateTime.parse("2025-10-28T17:00:00"),
                        EstadoTicket.FINALIZADO,
                        "Salida registrada"
                )
        );

        Mockito.when(vehiculoService.getHistorialVehiculo("AAA111"))
                .thenReturn(historial);

        mockMvc.perform(get("/vehiculo/historial/AAA111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vehiculo.patente", is("AAA111")))
                .andExpect(jsonPath("$[0].estadoTicket", is("EN_CURSO")))
                .andExpect(jsonPath("$[1].vehiculo.patente", is("AAA111")))
                .andExpect(jsonPath("$[1].estadoTicket", is("FINALIZADO")));
    }

}
