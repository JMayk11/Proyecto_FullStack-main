package com.perfulandia.microservicio_pagos.service;

import com.perfulandia.microservicio_pagos.entities.Pago;
import com.perfulandia.microservicio_pagos.repositories.PagoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class PagoServiceImplTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcesarPago_Exitoso() {
        Mockito.when(pagoRepository.save(Mockito.any(Pago.class))).thenAnswer(invocation -> {
            Pago p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        Pago resultado = pagoService.procesarPago(50L, 15000.0, "paypal");

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L, resultado.getId());
        Assertions.assertEquals(50L, resultado.getPedidoId());
        Assertions.assertEquals(15000.0, resultado.getMonto());
        Assertions.assertEquals("PAYPAL", resultado.getMetodoPago());
        Assertions.assertEquals("APROBADO", resultado.getEstado());
    }

    @Test
    public void testProcesarPago_MontoNull_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            pagoService.procesarPago(50L, null, "PAYPAL")
        );
    }

    @Test
    public void testProcesarPago_MontoNegativo_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            pagoService.procesarPago(50L, -100.0, "PAYPAL")
        );
    }

    @Test
    public void testProcesarPago_MetodoPagoNull_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            pagoService.procesarPago(50L, 100.0, null)
        );
    }

    @Test
    public void testProcesarPago_MetodoPagoVacio_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            pagoService.procesarPago(50L, 100.0, "   ")
        );
    }

    @Test
    public void testProcesarPago_PedidoIdNull_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            pagoService.procesarPago(null, 100.0, "TRANSFERENCIA")
        );
    }

    @Test
    public void testObtenerPagosPorPedido() {
        List<Pago> listaMock = new ArrayList<>();
        listaMock.add(new Pago());
        
        Mockito.when(pagoRepository.findByPedidoId(50L)).thenReturn(listaMock);

        List<Pago> resultado = pagoService.obtenerPagosPorPedido(50L);
        Assertions.assertFalse(resultado.isEmpty());
    }

    // 📌 TEST DE ENTIEDAD: Para pintar de verde cualquier Setter suelto
    @Test
    public void testPagoEntity_Setters() {
        Pago pago = new Pago();
        pago.setId(99L);
        pago.setPedidoId(88L);
        pago.setMonto(5000.0);
        pago.setMetodoPago("EFECTIVO");
        pago.setEstado("PENDIENTE");

        Assertions.assertEquals(99L, pago.getId());
        Assertions.assertEquals(88L, pago.getPedidoId());
        Assertions.assertEquals(5000.0, pago.getMonto());
        Assertions.assertEquals("EFECTIVO", pago.getMetodoPago());
        Assertions.assertEquals("PENDIENTE", pago.getEstado());
    }
}