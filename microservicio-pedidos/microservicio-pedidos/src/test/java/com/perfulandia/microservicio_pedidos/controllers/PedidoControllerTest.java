package com.perfulandia.microservicio_pedidos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.microservicio_pedidos.clients.PagoClient;
import com.perfulandia.microservicio_pedidos.clients.ProductoClient;
import com.perfulandia.microservicio_pedidos.dtos.CompraRequest;
import com.perfulandia.microservicio_pedidos.entities.PagoModel;
import com.perfulandia.microservicio_pedidos.entities.Pedido;
import com.perfulandia.microservicio_pedidos.entities.ProductoModel;
import com.perfulandia.microservicio_pedidos.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private ProductoClient productoClient;

    @MockBean
    private PagoClient pagoClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarPedidos() throws Exception {
        List<Pedido> lista = new ArrayList<>();
        lista.add(new Pedido(1L, "COMPLETADO", 50000.0));
        Mockito.when(pedidoRepository.findAll()).thenReturn(lista);

        mockMvc.perform(get("/pedidos/lista"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("COMPLETADO"));
    }

    @Test
    public void testSimularCompra_Exitoso() throws Exception {
        ProductoModel producto = new ProductoModel();
        producto.setId(100L);
        producto.setPrecio(35000.0);
        producto.setNombre("Perfume Test");

        Mockito.when(productoClient.obtenerProductoPorId(100L)).thenReturn(producto);

        mockMvc.perform(get("/pedidos/simular-compra/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(35000.0))
                .andExpect(jsonPath("$.estado").value("PROCESANDO_CON_FEIGN"));
    }

    @Test
    public void testSimularCompra_ErrorCliente() throws Exception {
        Mockito.when(productoClient.obtenerProductoPorId(999L))
               .thenThrow(new RuntimeException("API Error"));

        mockMvc.perform(get("/pedidos/simular-compra/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRealizarCompra_PagoAprobado() throws Exception {
        CompraRequest request = new CompraRequest();
        request.setUsuarioId(1L);
        request.setProductoId(100L);
        request.setCantidad(2);
        request.setMetodoPago("TARJETA");

        ProductoModel producto = new ProductoModel();
        producto.setId(100L);
        producto.setPrecio(10000.0);

        Pedido mockPedidoGuardado = new Pedido(1L, "PENDIENTE_DE_PAGO", 20000.0);
        mockPedidoGuardado.setId(77L);

        PagoModel mockPagoResponse = new PagoModel();
        mockPagoResponse.setEstado("APROBADO");

        Mockito.when(productoClient.obtenerProductoPorId(100L)).thenReturn(producto);
        Mockito.doNothing().when(productoClient).descontarStock(100L, 2);
        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenReturn(mockPedidoGuardado);
        Mockito.when(pagoClient.procesarPago(Mockito.any(PagoModel.class))).thenReturn(mockPagoResponse);

        mockMvc.perform(post("/pedidos/comprar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test
    public void testRealizarCompra_PagoRechazado() throws Exception {
        CompraRequest request = new CompraRequest();
        request.setUsuarioId(1L);
        request.setProductoId(100L);
        request.setCantidad(1);
        request.setMetodoPago("TARJETA");

        ProductoModel producto = new ProductoModel();
        producto.setId(100L);
        producto.setPrecio(10000.0);

        Pedido mockPedidoGuardado = new Pedido(1L, "PENDIENTE_DE_PAGO", 10000.0);
        mockPedidoGuardado.setId(77L);

        PagoModel mockPagoResponse = new PagoModel();
        mockPagoResponse.setEstado("RECHAZADO");

        Mockito.when(productoClient.obtenerProductoPorId(100L)).thenReturn(producto);
        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenReturn(mockPedidoGuardado);
        Mockito.when(pagoClient.procesarPago(Mockito.any(PagoModel.class))).thenReturn(mockPagoResponse);

        mockMvc.perform(post("/pedidos/comprar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("RECHAZADO"));
    }

    @Test
    public void testRealizarCompra_FaltaStock_Error() throws Exception {
        CompraRequest request = new CompraRequest();
        request.setProductoId(100L);
        request.setCantidad(50);

        Mockito.when(productoClient.obtenerProductoPorId(100L))
               .thenThrow(new RuntimeException("Stock Insuficiente"));

        mockMvc.perform(post("/pedidos/comprar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCrearPedidoManual() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(2L);
        pedido.setTotal(15000.0);

        Mockito.when(pedidoRepository.save(Mockito.any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidos/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerPorUsuario() throws Exception {
        List<Pedido> lista = new ArrayList<>();
        Mockito.when(pedidoRepository.findByUsuarioId(1L)).thenReturn(lista);

        mockMvc.perform(get("/pedidos/usuario/1"))
                .andExpect(status().isOk());
    }
}