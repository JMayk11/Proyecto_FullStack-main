package com.perfulandia.microservicio_notificaciones.service;

import com.perfulandia.microservicio_notificaciones.dto.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public String enviarCorreoElectronico(NotificationRequest request) {
        // 1. Validar objeto base y destinatario
        if (request == null || request.getDestinatario() == null || request.getDestinatario().trim().isEmpty()) {
            throw new IllegalArgumentException("Error: El destinatario no puede estar vacío.");
        }

        // 2. Validar asunto
        if (request.getAsunto() == null || request.getAsunto().trim().isEmpty()) {
            throw new IllegalArgumentException("Error: El asunto no puede estar vacío.");
        }

        // 3. Validar mensaje
        if (request.getMensaje() == null || request.getMensaje().trim().isEmpty()) {
            throw new IllegalArgumentException("Error: El mensaje no puede estar vacío.");
        }

        System.out.println("=========================================================");
        System.out.println("[SMTP MAIL SERVER] DESPACHANDO CORREO ELECTRÓNICO...");
        System.out.println("Para: " + request.getDestinatario());
        System.out.println("Asunto: " + request.getAsunto());
        System.out.println("Cuerpo: " + request.getMensaje());
        System.out.println("[STATUS] ¡Correo enviado con éxito!");
        System.out.println("=========================================================");

        return "Notificación enviada exitosamente al usuario.";
    }
}