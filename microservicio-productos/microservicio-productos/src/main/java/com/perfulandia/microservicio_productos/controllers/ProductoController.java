package com.perfulandia.microservicio_productos.controllers;

import com.perfulandia.microservicio_productos.entities.Producto;
import com.perfulandia.microservicio_productos.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/lista")
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    // Endpoint que usa Feign Client para leer los datos del perfume al iniciar la compra
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> ResponseEntity.ok(producto))
                .orElse(ResponseEntity.notFound().build());
    }

    /* 👇 NUEVA FUNCIONALIDAD: Endpoint inteligente que descuenta y valida stock */
    @PutMapping("/{id}/descontar-stock")
    public ResponseEntity<?> descontarStock(
            @PathVariable Long id, 
            @RequestParam Integer cantidad) {
        try {
            // 1. Buscamos el perfume por ID en la base de datos
            Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
            
            // 2. Validamos si hay suficientes existencias en el inventario
            if (producto.getStock() < cantidad) {
                return ResponseEntity.badRequest()
                    .body("Stock insuficiente para " + producto.getNombre() + ". Stock disponible: " + producto.getStock());
            }
            
            // 3. Restamos la cantidad solicitada y guardamos el cambio en la persistencia
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
            
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar el inventario: " + e.getMessage());
        }
    }
}