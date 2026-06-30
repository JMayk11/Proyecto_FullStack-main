package com.perfulandia.microservicio_pedidos.service;

import com.perfulandia.microservicio_pedidos.dtos.CompraRequest;
import com.perfulandia.microservicio_pedidos.entities.PagoModel;
import com.perfulandia.microservicio_pedidos.entities.Pedido;
import com.perfulandia.microservicio_pedidos.entities.ProductoModel;
import com.perfulandia.microservicio_pedidos.repositories.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearPedido_Exitoso() {
        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenAnswer(i -> i.getArgument(0));

        Pedido resultado = pedidoService.crearPedido(1L, 25000.0);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L, resultado.getUsuarioId());
        Assertions.assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    public void testCrearPedido_MontoInvalido_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedido(1L, 0.0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedido(1L, null));
    }

    @Test
    public void testCrearPedido_UsuarioNull_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedido(null, 500.0));
    }

    @Test
    public void testActualizarEstado_Exitoso() {
        Pedido mockPedido = new Pedido(1L, "PENDIENTE", 1000.0);
        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(mockPedido));
        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenReturn(mockPedido);

        Pedido resultado = pedidoService.actualizarEstado(1L, "PAGADO");
        Assertions.assertEquals("PAGADO", resultado.getEstado());
    }

    @Test
    public void testActualizarEstado_NotFound_Exception() {
        Mockito.when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> pedidoService.actualizarEstado(99L, "PAGADO"));
    }

    @Test
    public void testCancelarPedidoPorInactividad() {
        Pedido mockPedido = new Pedido(1L, "PENDIENTE", 1000.0);
        Mockito.when(pedidoRepository.findById(1L)).thenReturn(Optional.of(mockPedido));
        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenReturn(mockPedido);

        Pedido resultado = pedidoService.cancelarPedidoPorInactividad(1L);
        Assertions.assertEquals("CANCELADO", resultado.getEstado());
    }

    // 📌 COBERTURA COMPLETA DE MODELOS (Entities y DTOs)
    @Test
    public void testModelosGetterSetters() {
        // 1. PagoModel
        PagoModel pago = new PagoModel();
        pago.setId(1L);
        pago.setPedidoId(2L);
        pago.setMonto(150.0);
        pago.setMetodoPago("EFECTIVO");
        pago.setEstado("APROBADO");

        Assertions.assertEquals(1L, pago.getId());
        Assertions.assertEquals(2L, pago.getPedidoId());
        Assertions.assertEquals(150.0, pago.getMonto());
        Assertions.assertEquals("EFECTIVO", pago.getMetodoPago());
        Assertions.assertEquals("APROBADO", pago.getEstado());

        // 2. ProductoModel
        ProductoModel producto = new ProductoModel();
        producto.setId(10L);
        producto.setNombre("Chanel");
        producto.setPrecio(120.0);

        Assertions.assertEquals(10L, producto.getId());
        Assertions.assertEquals("Chanel", producto.getNombre());
        Assertions.assertEquals(120.0, producto.getPrecio());

        // 3. CompraRequest
        CompraRequest request = new CompraRequest();
        request.setUsuarioId(5L);
        request.setProductoId(25L);
        request.setCantidad(3);
        request.setMetodoPago("WEBPAY");

        Assertions.assertEquals(5L, request.getUsuarioId());
        Assertions.assertEquals(25L, request.getProductoId());
        Assertions.assertEquals(3, request.getCantidad());
        Assertions.assertEquals("WEBPAY", request.getMetodoPago());

        // 4. Pedido Entity constructor vacío y setters faltantes
        Pedido pedido = new Pedido();
        java.time.LocalDateTime fecha = java.time.LocalDateTime.now();
        pedido.setId(1L);
        pedido.setFecha(fecha);

        Assertions.assertEquals(1L, pedido.getId());
        Assertions.assertEquals(fecha, pedido.getFecha());
    }
}