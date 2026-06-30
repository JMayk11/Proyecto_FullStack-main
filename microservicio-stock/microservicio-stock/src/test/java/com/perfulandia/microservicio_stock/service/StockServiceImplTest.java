package com.perfulandia.microservicio_stock.service;

import com.perfulandia.microservicio_stock.entities.Stock;
import com.perfulandia.microservicio_stock.repositories.StockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    @Test
    public void testObtenerStockPorProducto_Exitoso() {
        Stock stock = new Stock(1L, 100, 10);
        Mockito.when(stockRepository.findByProductoId(1L)).thenReturn(Optional.of(stock));

        Stock resultado = stockService.obtenerStockPorProducto(1L);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(100, resultado.getCantidad());
    }

    @Test
    public void testObtenerStockPorProducto_Inexistente_Exception() {
        Mockito.when(stockRepository.findByProductoId(99L)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> stockService.obtenerStockPorProducto(99L));
    }

    @Test
    public void testActualizarStock_Existente_Exitoso() {
        Stock stockExistente = new Stock(1L, 50, 5);
        Mockito.when(stockRepository.findByProductoId(1L)).thenReturn(Optional.of(stockExistente));
        Mockito.when(stockRepository.save(Mockito.any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stock resultado = stockService.actualizarStock(1L, 80);
        Assertions.assertEquals(80, resultado.getCantidad());
    }

    @Test
    public void testActualizarStock_NoExistente_CreaNuevo() {
        Mockito.when(stockRepository.findByProductoId(2L)).thenReturn(Optional.empty());
        Mockito.when(stockRepository.save(Mockito.any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stock resultado = stockService.actualizarStock(2L, 15);
        Assertions.assertEquals(2L, resultado.getProductoId());
        Assertions.assertEquals(15, resultado.getCantidad());
    }

    @Test
    public void testActualizarStock_CantidadInvalida_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> stockService.actualizarStock(1L, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> stockService.actualizarStock(1L, -10));
    }

    @Test
    public void testVerificarDisponibilidad_Exitoso_True() {
        Stock stock = new Stock(1L, 30, 5);
        Mockito.when(stockRepository.findByProductoId(1L)).thenReturn(Optional.of(stock));

        boolean resultado = stockService.verificarDisponibilidad(1L, 20);
        Assertions.assertTrue(resultado);
    }

    @Test
    public void testVerificarDisponibilidad_Insuficiente_False() {
        Stock stock = new Stock(1L, 10, 5);
        Mockito.when(stockRepository.findByProductoId(1L)).thenReturn(Optional.of(stock));

        boolean resultado = stockService.verificarDisponibilidad(1L, 50);
        Assertions.assertFalse(resultado);
    }

    @Test
    public void testVerificarDisponibilidad_CantidadRequeridaInvalida_False() {
        Assertions.assertFalse(stockService.verificarDisponibilidad(1L, null));
        Assertions.assertFalse(stockService.verificarDisponibilidad(1L, 0));
        Assertions.assertFalse(stockService.verificarDisponibilidad(1L, -5));
    }

    @Test
    public void testVerificarDisponibilidad_NoExistente_False() {
        Mockito.when(stockRepository.findByProductoId(99L)).thenReturn(Optional.empty());
        boolean resultado = stockService.verificarDisponibilidad(99L, 10);
        Assertions.assertFalse(resultado);
    }

    // 📌 COBERTURA DE ENTIDAD STOCK (Getters, Setters y Constructores huerfanos)
    @Test
    public void testStockEntity_GettersAndSetters() {
        Stock s = new Stock();
        s.setId(10L);
        s.setProductoId(5L);
        s.setCantidad(150);
        s.setStockMinimo(2);

        Assertions.assertEquals(10L, s.getId());
        Assertions.assertEquals(5L, s.getProductoId());
        Assertions.assertEquals(150, s.getCantidad());
        Assertions.assertEquals(2, s.getStockMinimo());
    }
}