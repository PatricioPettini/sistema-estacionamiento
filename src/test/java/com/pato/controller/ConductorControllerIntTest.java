package com.pato.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pato.dto.request.ConductorRequestDTO;
import com.pato.model.Conductor;
import com.pato.repository.IConductorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ConductorControllerIntTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IConductorRepository conductorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/conductor"; // ✅ URL dinámica

        conductorRepository.deleteAll();

        conductorRepository.save(new Conductor(null, "Juan", "Pérez", "12345678"));
    }

    @Test
    void getAllConductores_deberiaRetornarConductoresDesdeLaBD() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Juan"));
    }

    @Test
    void editarConductor_deberiaActualizarDatos() throws Exception {
        ConductorRequestDTO dto = new ConductorRequestDTO("Carlos", "López", "12345678");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/editar/1",
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Carlos"));
    }
}
