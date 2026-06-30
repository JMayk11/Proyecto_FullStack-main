package com.perfulandia.microservicio.envios.controllers;

import com.perfulandia.microservicio.envios.entities.Envio;
import com.perfulandia.microservicio.envios.service.EnvioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnvioController.class)
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService envioService;

    @Test
    public void testCrearEnvio_Exitoso() throws Exception {
        Envio mockEnvio = new Envio(101L, "TRK-12345678", "Metropolitana", "En preparación");
        mockEnvio.setId(1L);

        Mockito.when(envioService.registrarEnvio(101L, "Metropolitana")).thenReturn(mockEnvio);

        mockMvc.perform(post("/api/envios/crear")
                .param("pedidoId", "101")
                .param("region", "Metropolitana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroTracking").value("TRK-12345678"))
                .andExpect(jsonPath("$.estadoLogistico").value("En preparación"));
    }

    @Test
    public void testCrearEnvio_BadRequest() throws Exception {
        Mockito.when(envioService.registrarEnvio(Mockito.any(), Mockito.any()))
               .thenThrow(new IllegalArgumentException("La región de destino no puede estar vacía."));

        mockMvc.perform(post("/api/envios/crear")
                .param("pedidoId", "101")
                .param("region", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testActualizarEstado_Exitoso() throws Exception {
        Envio mockEnvio = new Envio(101L, "TRK-12345678", "Metropolitana", "En Ruta");
        mockEnvio.setId(1L);

        Mockito.when(envioService.actualizarEstadoLogistico(1L, "En Ruta")).thenReturn(mockEnvio);

        mockMvc.perform(put("/api/envios/actualizar/1")
                .param("nuevoEstado", "En Ruta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoLogistico").value("En Ruta"));
    }

    @Test
    public void testActualizarEstado_BadRequest() throws Exception {
        Mockito.when(envioService.actualizarEstadoLogistico(Mockito.anyLong(), Mockito.anyString()))
               .thenThrow(new IllegalArgumentException("No se encontró el registro de envío con ID: 99"));

        mockMvc.perform(put("/api/envios/actualizar/99")
                .param("nuevoEstado", "En Ruta"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testObtenerPorPedido_Exitoso() throws Exception {
        Envio mockEnvio = new Envio(101L, "TRK-12345678", "Metropolitana", "En preparación");
        mockEnvio.setId(1L);

        Mockito.when(envioService.obtenerEnvioPorPedido(101L)).thenReturn(mockEnvio);

        mockMvc.perform(get("/api/envios/pedido/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(101));
    }

    @Test
    public void testObtenerPorPedido_NotFound() throws Exception {
        Mockito.when(envioService.obtenerEnvioPorPedido(999L))
               .thenThrow(new IllegalArgumentException("No hay despachos asociados"));

        mockMvc.perform(get("/api/envios/pedido/999"))
                .andExpect(status().isNotFound());
    }
}