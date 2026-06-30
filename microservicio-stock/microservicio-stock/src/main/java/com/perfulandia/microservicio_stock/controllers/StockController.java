package com.perfulandia.microservicio_stock.controllers;

import com.perfulandia.microservicio_stock.entities.Stock;
import com.perfulandia.microservicio_stock.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Stock> obtenerStock(@PathVariable Long productoId) {
        try {
            Stock stock = stockService.obtenerStockPorProducto(productoId);
            return ResponseEntity.ok(stock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/producto/{productoId}")
    public ResponseEntity<?> actualizarStock(@PathVariable Long productoId, @RequestParam Integer cantidad) {
        try {
            Stock stockActualizado = stockService.actualizarStock(productoId, cantidad);
            return ResponseEntity.ok(stockActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/validar")
    public ResponseEntity<Boolean> validarDisponibilidad(@RequestParam Long productoId, @RequestParam Integer cantidad) {
        boolean disponible = stockService.verificarDisponibilidad(productoId, cantidad);
        return ResponseEntity.ok(disponible);
    }
}