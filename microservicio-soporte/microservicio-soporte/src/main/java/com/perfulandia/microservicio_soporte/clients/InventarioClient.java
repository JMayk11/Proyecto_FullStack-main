package com.perfulandia.microservicio_soporte.clients; // Tu package correspondiente

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservicio-stock", path = "/api/stock")
public interface InventarioClient {

    // Cambiado de "/reingreso/{id}" a "/producto/{productoId}" para coincidir con tu StockController
    @PutMapping("/producto/{productoId}")
    ResponseEntity<?> reingresarStockFisico(
        @PathVariable("productoId") Long productoId, 
        @RequestParam("cantidad") Integer cantidad
    );
}