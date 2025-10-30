package com.pato.validation;

import com.pato.model.enums.EstadoTicket;
import com.pato.repository.ITicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketValidatorTest {

    @Mock
    private ITicketRepository ticketRepository;

    private TicketValidator ticketValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketValidator = new TicketValidator(ticketRepository);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ticket ya está finalizado")
    void validarTicketFinalizado_DeberiaLanzarExcepcionSiYaFinalizado() {
        Long idTicket = 1L;

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> ticketValidator.validarTicketFinalizado(idTicket, EstadoTicket.FINALIZADO)
        );

        assertEquals("El ticket ya está finalizado.", exception.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción si el ticket no está finalizado")
    void validarTicketFinalizado_NoDebeLanzarExcepcionSiNoFinalizado() {
        assertDoesNotThrow(() ->
                ticketValidator.validarTicketFinalizado(1L, EstadoTicket.EN_CURSO)
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción si ya existe ticket en curso para la patente")
    void validarVehiculoEnCurso_DeberiaLanzarExcepcionSiExiste() {
        String patente = "ABC123";
        when(ticketRepository.existsByVehiculoPatenteAndEstadoTicket(patente, EstadoTicket.EN_CURSO))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ticketValidator.validarVehiculoEnCurso(patente, EstadoTicket.EN_CURSO)
        );

        assertEquals("Ya existe un ticket en curso para este vehículo.", exception.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción si no existe ticket en curso para la patente")
    void validarVehiculoEnCurso_NoDebeLanzarExcepcionSiNoExiste() {
        String patente = "XYZ789";
        when(ticketRepository.existsByVehiculoPatenteAndEstadoTicket(patente, EstadoTicket.EN_CURSO))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                ticketValidator.validarVehiculoEnCurso(patente, EstadoTicket.EN_CURSO)
        );
    }

    @Test
    @DisplayName("Debe lanzar excepción si ya existe ticket en curso para el conductor")
    void validarConductorEnCurso_DeberiaLanzarExcepcionSiExiste() {
        String dni = "12345678";
        when(ticketRepository.existsByConductorDniAndEstadoTicket(dni, EstadoTicket.EN_CURSO))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ticketValidator.validarConductorEnCurso(dni, EstadoTicket.EN_CURSO)
        );

        assertEquals("Ya existe un ticket en curso para este conductor.", exception.getMessage());
    }

    @Test
    @DisplayName("No debe lanzar excepción si no existe ticket en curso para el conductor")
    void validarConductorEnCurso_NoDebeLanzarExcepcionSiNoExiste() {
        String dni = "87654321";
        when(ticketRepository.existsByConductorDniAndEstadoTicket(dni, EstadoTicket.EN_CURSO))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                ticketValidator.validarConductorEnCurso(dni, EstadoTicket.EN_CURSO)
        );
    }
}
