package com.perfulandia.microservicio.envios.service;

import com.perfulandia.microservicio.envios.entities.Envio;
import com.perfulandia.microservicio.envios.repositories.EnvioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class EnvioServiceImplTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioServiceImpl envioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarEnvio_Exitoso() {
        Mockito.when(envioRepository.save(Mockito.any(Envio.class))).thenAnswer(invocation -> {
            Envio argumento = invocation.getArgument(0);
            argumento.setId(1L);
            return argumento;
        });

        Envio resultado = envioService.registrarEnvio(101L, "Valparaíso");

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L, resultado.getId());
        Assertions.assertEquals(101L, resultado.getPedidoId());
        Assertions.assertEquals("Valparaíso", resultado.getRegion());
        Assertions.assertTrue(resultado.getNumeroTracking().startsWith("TRK-"));
        Assertions.assertEquals("En preparación", resultado.getEstadoLogistico());
    }

    @Test
    public void testRegistrarEnvio_PedidoIdNull_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            envioService.registrarEnvio(null, "Valparaíso")
        );
    }

    @Test
    public void testRegistrarEnvio_RegionVacia_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            envioService.registrarEnvio(101L, "   ")
        );
    }

    // 📌 NUEVO TEST: Cubre la línea roja 22 cuando la región es enteramente NULL
    @Test
    public void testRegistrarEnvio_RegionNull_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            envioService.registrarEnvio(101L, null)
        );
    }

    @Test
    public void testActualizarEstadoLogistico_Exitoso() {
        Envio mockEnvio = new Envio(101L, "TRK-8888", "Biobío", "En preparación");
        mockEnvio.setId(1L);

        Mockito.when(envioRepository.findById(1L)).thenReturn(Optional.of(mockEnvio));
        Mockito.when(envioRepository.save(Mockito.any(Envio.class))).thenReturn(mockEnvio);

        Envio resultado = envioService.actualizarEstadoLogistico(1L, "Entregado");

        Assertions.assertEquals("Entregado", resultado.getEstadoLogistico());
    }

    @Test
    public void testActualizarEstadoLogistico_NoEncontrado_Exception() {
        Mockito.when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            envioService.actualizarEstadoLogistico(99L, "En Ruta")
        );
    }

    @Test
    public void testObtenerEnvioPorPedido_Exitoso() {
        Envio mockEnvio = new Envio(101L, "TRK-8888", "Biobío", "En preparación");
        
        Mockito.when(envioRepository.findByPedidoId(101L)).thenReturn(Optional.of(mockEnvio));

        Envio resultado = envioService.obtenerEnvioPorPedido(101L);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(101L, resultado.getPedidoId());
    }

    @Test
    public void testObtenerEnvioPorPedido_NoEncontrado_Exception() {
        Mockito.when(envioRepository.findByPedidoId(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            envioService.obtenerEnvioPorPedido(999L)
        );
    }

    // 📌 NUEVO TEST: Cubre la línea roja 42 de Envio.java (setNumeroTracking)
    @Test
    public void testEnvioEntity_SetNumeroTracking() {
        Envio envio = new Envio();
        envio.setNumeroTracking("TRK-NUEVO123");
        Assertions.assertEquals("TRK-NUEVO123", envio.getNumeroTracking());
    }

    // 📌 NUEVO TEST: Cubre la línea roja 45 de Envio.java (setRegion)
    @Test
    public void testEnvioEntity_SetRegion() {
        Envio envio = new Envio();
        envio.setRegion("Antofagasta");
        Assertions.assertEquals("Antofagasta", envio.getRegion());
    }
}