package com.perfulandia.microservicio_stock.controllers;

import com.perfulandia.microservicio_stock.entities.Stock;
import com.perfulandia.microservicio_stock.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(StockController.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Test
    public void testObtenerStock_Exitoso() throws Exception {
        Stock stock = new Stock(1L, 50, 5);
        Mockito.when(stockService.obtenerStockPorProducto(1L)).thenReturn(stock);

        mockMvc.perform(get("/api/stock/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(50));
    }

    @Test
    public void testObtenerStock_NotFound_Exception() throws Exception {
        Mockito.when(stockService.obtenerStockPorProducto(99L))
               .thenThrow(new IllegalArgumentException("No se encontró registro"));

        mockMvc.perform(get("/api/stock/producto/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testActualizarStock_Exitoso() throws Exception {
        Stock stock = new Stock(1L, 20, 5);
        Mockito.when(stockService.actualizarStock(1L, 20)).thenReturn(stock);

        mockMvc.perform(put("/api/stock/producto/1")
                .param("cantidad", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad").value(20));
    }

    @Test
    public void testActualizarStock_BadRequest_Exception() throws Exception {
        Mockito.when(stockService.actualizarStock(1L, -5))
               .thenThrow(new IllegalArgumentException("La cantidad de stock no puede ser negativa."));

        mockMvc.perform(put("/api/stock/producto/1")
                .param("cantidad", "-5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La cantidad de stock no puede ser negativa."));
    }

    @Test
    public void testValidarDisponibilidad_True() throws Exception {
        Mockito.when(stockService.verificarDisponibilidad(1L, 10)).thenReturn(true);

        mockMvc.perform(get("/api/stock/validar")
                .param("productoId", "1")
                .param("cantidad", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}