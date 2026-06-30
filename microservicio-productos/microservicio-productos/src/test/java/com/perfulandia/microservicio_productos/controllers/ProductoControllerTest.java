package com.perfulandia.microservicio_productos.controllers;

import com.perfulandia.microservicio_productos.entities.Producto;
import com.perfulandia.microservicio_productos.repositories.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository productoRepository;

    @Test
    public void testListarProductos() throws Exception {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto("Sauvage", "Dior", 130.0, 10, "Fresco"));
        
        Mockito.when(productoRepository.findAll()).thenReturn(lista);

        mockMvc.perform(get("/productos/lista"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Sauvage"));
    }

    @Test
    public void testObtenerPorId_Exitoso() throws Exception {
        Producto producto = new Producto("Bleu de Chanel", "Chanel", 140.0, 5, "Amaderado");
        producto.setId(1L);

        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bleu de Chanel"));
    }

    @Test
    public void testObtenerPorId_NotFound() throws Exception {
        Mockito.when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDescontarStock_Exitoso() throws Exception {
        Producto producto = new Producto("Good Girl", "Carolina Herrera", 135.0, 10, "Audaz");
        producto.setId(2L);

        Mockito.when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        Mockito.when(productoRepository.save(Mockito.any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/productos/2/descontar-stock")
                .param("cantidad", "4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(6)); // 10 - 4 = 6
    }

    @Test
    public void testDescontarStock_Insuficiente_BadRequest() throws Exception {
        Producto producto = new Producto("Black Opium", "YSL", 145.0, 3, "Adictivo");
        producto.setId(3L);

        Mockito.when(productoRepository.findById(3L)).thenReturn(Optional.of(producto));

        mockMvc.perform(put("/productos/3/descontar-stock")
                .param("cantidad", "5") // Pide 5 y solo hay 3
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDescontarStock_RuntimeException_ServerError() throws Exception {
        Mockito.when(productoRepository.findById(5L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/productos/5/descontar-stock")
                .param("cantidad", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}