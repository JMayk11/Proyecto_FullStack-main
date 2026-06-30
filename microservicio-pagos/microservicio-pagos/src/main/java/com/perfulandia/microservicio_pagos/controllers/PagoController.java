package com.perfulandia.microservicio_pagos.controllers;

import com.perfulandia.microservicio_pagos.entities.Pago;
import com.perfulandia.microservicio_pagos.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    // Inyección limpia por constructor (Buenas prácticas)
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Endpoint conectado al servicio con control de excepciones
    @PostMapping("/procesar")
    public ResponseEntity<?> procesarPago(@RequestBody Pago pago) {
        try {
            Pago pagoProcesado = pagoService.procesarPago(pago.getPedidoId(), pago.getMonto(), pago.getMetodoPago());
            return ResponseEntity.ok(pagoProcesado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Nota: Si necesitas listar todos, dejamos el repositorio o servicio. 
    // Para no romper tu estructura, puedes mantenerlo llamando al servicio o directo si no hay lógica.
}