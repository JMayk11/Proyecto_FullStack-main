package com.perfulandia.microservicio_notificaciones.controllers;

import com.perfulandia.microservicio_notificaciones.dto.NotificationRequest;
import com.perfulandia.microservicio_notificaciones.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/enviar-correo")
    public ResponseEntity<String> enviarCorreoElectronico(@RequestBody NotificationRequest request) {
        try {
            String respuesta = notificationService.enviarCorreoElectronico(request);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}