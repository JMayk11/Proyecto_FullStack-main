package com.perfulandia.microservicio_pagos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.microservicio_pagos.entities.Pago;
import com.perfulandia.microservicio_pagos.service.PagoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testProcesarPago_Exitoso() throws Exception {
        Pago pagoRequest = new Pago();
        pagoRequest.setPedidoId(10L);
        pagoRequest.setMonto(25000.0);
        pagoRequest.setMetodoPago("TARJETA_CREDITO");

        Pago mockPagoGuardado = new Pago();
        mockPagoGuardado.setId(1L);
        mockPagoGuardado.setPedidoId(10L);
        mockPagoGuardado.setMonto(25000.0);
        mockPagoGuardado.setMetodoPago("TARJETA_CREDITO");
        mockPagoGuardado.setEstado("APROBADO");

        Mockito.when(pagoService.procesarPago(10L, 25000.0, "TARJETA_CREDITO"))
               .thenReturn(mockPagoGuardado);

        mockMvc.perform(post("/pagos/procesar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("APROBADO"));
    }

    @Test
    public void testProcesarPago_BadRequest_MontoInvalido() throws Exception {
        Pago pagoRequest = new Pago();
        pagoRequest.setPedidoId(10L);
        pagoRequest.setMonto(-500.0);
        pagoRequest.setMetodoPago("PAYPAL");

        Mockito.when(pagoService.procesarPago(Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyString()))
               .thenThrow(new IllegalArgumentException("El monto a facturar debe ser estrictamente mayor a cero."));

        mockMvc.perform(post("/pagos/procesar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagoRequest)))
                .andExpect(status().isBadRequest());
    }
}