package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.VehiculoRequestDTO;
import com.pato.dto.response.ConductorResponseDTO;
import com.pato.dto.response.TicketResponseDTO;
import com.pato.dto.response.VehiculoResponseDTO;
import com.pato.model.enums.EstadoTicket;
import com.pato.model.enums.TipoVehiculo;
import com.pato.service.interfaces.IVehiculoService;
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

@WebMvcTest(VehiculoController.class)
class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IVehiculoService vehiculoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllVehiculos_deberiaRetornarListaDeVehiculos() throws Exception {
        var v1 = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "ABC123");
        var v2 = new VehiculoResponseDTO(2L, TipoVehiculo.MOTO, "XYZ789");

        Mockito.when(vehiculoService.getAllVehiculos())
                .thenReturn(List.of(v1, v2));

        mockMvc.perform(get("/vehiculo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].patente", is("ABC123")))
                .andExpect(jsonPath("$[1].tipo", is("MOTO")));
    }

    @Test
    void getVehiculo_porPatente_deberiaRetornarVehiculo() throws Exception {
        var vehiculo = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "ABC123");
        Mockito.when(vehiculoService.getVehiculo("ABC123"))
                .thenReturn(Optional.of(vehiculo));

        mockMvc.perform(get("/vehiculo/get/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patente", is("ABC123")))
                .andExpect(jsonPath("$.tipo", is("AUTO")));
    }

    @Test
    void editarVehiculo_deberiaActualizarYRetornarVehiculoEditado() throws Exception {
        var request = new VehiculoRequestDTO(TipoVehiculo.AUTO, "ZZZ999");
        var response = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "ZZZ999");

        Mockito.when(vehiculoService.editarVehiculo(Mockito.eq(1L), Mockito.any(VehiculoRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/vehiculo/editar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patente", is("ZZZ999")))
                .andExpect(jsonPath("$.tipo", is("AUTO")));
    }

    @Test
    void getHistorialVehiculo_deberiaRetornarListaDeTickets() throws Exception {
        var vehiculo = new VehiculoResponseDTO(1L, TipoVehiculo.AUTO, "ABC123");
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

        Mockito.when(vehiculoService.getHistorialVehiculo("ABC123"))
                .thenReturn(List.of(ticket));

        mockMvc.perform(get("/vehiculo/historial/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vehiculo.patente", is("ABC123")))
                .andExpect(jsonPath("$[0].estadoTicket", is("EN_CURSO")))
                .andExpect(jsonPath("$[0].conductor.nombre", is("Juan")));
    }
}
