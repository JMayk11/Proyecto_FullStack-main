package com.perfulandia.microservicio_notificaciones.service;

import com.perfulandia.microservicio_notificaciones.dto.NotificationRequest;

public interface NotificationService {
    String enviarCorreoElectronico(NotificationRequest request);
}