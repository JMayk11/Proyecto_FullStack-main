package com.perfulandia.microservicio.envios.controllers;

import com.perfulandia.microservicio.envios.entities.Envio;
import com.perfulandia.microservicio.envios.service.EnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearEnvio(@RequestParam Long pedidoId, @RequestParam String region) {
        try {
            Envio nuevoEnvio = envioService.registrarEnvio(pedidoId, region);
            return ResponseEntity.ok(nuevoEnvio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar/{envioId}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long envioId, @RequestParam String nuevoEstado) {
        try {
            Envio envioActualizado = envioService.actualizarEstadoLogistico(envioId, nuevoEstado);
            return ResponseEntity.ok(envioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Envio> obtenerPorPedido(@PathVariable Long pedidoId) {
        try {
            Envio envio = envioService.obtenerEnvioPorPedido(pedidoId);
            return ResponseEntity.ok(envio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}