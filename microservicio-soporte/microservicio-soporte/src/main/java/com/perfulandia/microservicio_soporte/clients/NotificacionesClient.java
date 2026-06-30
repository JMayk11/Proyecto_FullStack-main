package com.perfulandia.microservicio_soporte.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "microservicio-notificaciones")
public interface NotificacionesClient {

    @PostMapping("/api/notificaciones/enviar-correo")
    void enviarCorreo(@RequestBody Map<String, String> request);
}