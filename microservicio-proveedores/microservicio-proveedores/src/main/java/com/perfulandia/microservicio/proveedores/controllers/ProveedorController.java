package com.perfulandia.microservicio.proveedores.controllers;

import com.perfulandia.microservicio.proveedores.entities.Proveedor;
import com.perfulandia.microservicio.proveedores.service.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    // Inyección del servicio a través del constructor
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    /**
     * Endpoint para registrar un nuevo proveedor en el sistema
     * POST http://localhost:8087/api/proveedores/crear
     */
    @PostMapping("/crear")
    public ResponseEntity<?> registrarProveedor(@RequestBody Proveedor proveedor) {
        try {
            Proveedor nuevoProveedor = proveedorService.registrarProveedor(proveedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProveedor);
        } catch (IllegalArgumentException e) {
            // Si salta alguna regla de negocio del servicio, capturamos el mensaje y enviamos un 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para buscar un proveedor filtrando por la marca que distribuye
     * GET http://localhost:8087/api/proveedores/marca/{nombreMarca}
     */
    @GetMapping("/marca/{nombreMarca}")
    public ResponseEntity<?> obtenerPorMarca(@PathVariable String nombreMarca) {
        try {
            Proveedor proveedor = proveedorService.obtenerProveedorPorMarca(nombreMarca);
            return ResponseEntity.ok(proveedor);
        } catch (IllegalArgumentException e) {
            // Si la marca no existe, devolvemos un estado 404 Not Found con el mensaje
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}