package com.perfulandia.microservicio_pedidos.clients;

import com.perfulandia.microservicio_pedidos.entities.ProductoModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservicio-productos")
public interface ProductoClient {

    @GetMapping("/productos/{id}")
    ProductoModel obtenerProductoPorId(@PathVariable("id") Long id);

    /* 🔴 EL MÉTODO QUE FALTABA: Con esto PedidoController sabrá cómo comunicarse con Productos */
    @PutMapping("/productos/{id}/descontar-stock")
    void descontarStock(@PathVariable("id") Long id, @RequestParam("cantidad") Integer cantidad);
}