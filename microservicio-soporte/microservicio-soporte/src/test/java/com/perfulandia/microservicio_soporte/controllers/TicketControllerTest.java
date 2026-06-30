package com.perfulandia.microservicio_soporte.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.microservicio_soporte.entities.Ticket;
import com.perfulandia.microservicio_soporte.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@ActiveProfiles("test")
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAbrirTicket_Exitoso() throws Exception {
        Ticket ticketInput = new Ticket();
        ticketInput.setPedidoId(1024L);
        ticketInput.setTipoUrgencia("PRODUCTO_DAÑADO");

        Ticket ticketOutput = new Ticket();
        ticketOutput.setId(1L);
        ticketOutput.setPedidoId(1024L);
        ticketOutput.setPrioridad("ALTA");
        ticketOutput.setEstado("ABIERTO");

        Mockito.when(ticketService.abrirTicket(Mockito.any(Ticket.class))).thenReturn(ticketOutput);

        mockMvc.perform(post("/api/soporte/ticket/abrir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.prioridad").value("ALTA"));
    }

    @Test
    public void testAbrirTicket_BadRequest() throws Exception {
        Mockito.when(ticketService.abrirTicket(Mockito.any(Ticket.class)))
                .thenThrow(new IllegalArgumentException("El tipo de urgencia es obligatorio."));

        mockMvc.perform(post("/api/soporte/ticket/abrir")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testProcesarDevolucion_Exitoso() throws Exception {
        Ticket ticketOutput = new Ticket();
        ticketOutput.setId(1L);
        ticketOutput.setEstado("EN_PROCESO");

        Mockito.when(ticketService.procesarDevolucionFisica(1L, 5L, 2)).thenReturn(ticketOutput);

        mockMvc.perform(put("/api/soporte/ticket/1/devolucion/5")
                .param("cantidad", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    @Test
    public void testProcesarDevolucion_BadRequest() throws Exception {
        Mockito.when(ticketService.procesarDevolucionFisica(1L, 5L, -1))
                .thenThrow(new IllegalArgumentException("La cantidad debe ser mayor a cero."));

        mockMvc.perform(put("/api/soporte/ticket/1/devolucion/5")
                .param("cantidad", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCerrarTicket_Exitoso() throws Exception {
        Ticket ticketOutput = new Ticket();
        ticketOutput.setId(1L);
        ticketOutput.setEstado("CERRADO");

        Mockito.when(ticketService.cerrarTicket(1L)).thenReturn(ticketOutput);

        mockMvc.perform(put("/api/soporte/ticket/1/cerrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CERRADO"));
    }

    @Test
    public void testCerrarTicket_NotFound() throws Exception {
        Mockito.when(ticketService.cerrarTicket(99L))
                .thenThrow(new IllegalArgumentException("No se encontró el ticket con ID: 99"));

        mockMvc.perform(put("/api/soporte/ticket/99/cerrar"))
                .andExpect(status().isNotFound());
    }
}