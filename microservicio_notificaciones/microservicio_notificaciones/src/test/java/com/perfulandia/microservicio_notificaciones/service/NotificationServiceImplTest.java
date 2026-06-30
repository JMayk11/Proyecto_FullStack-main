package com.perfulandia.microservicio_notificaciones.service;

import com.perfulandia.microservicio_notificaciones.dto.NotificationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotificationServiceImplTest {

    private final NotificationService notificationService = new NotificationServiceImpl();

    @Test
    public void testEnviarCorreo_Exitoso() {
        NotificationRequest request = new NotificationRequest("test@perfulandia.com", "Asunto", "Mensaje");
        
        String resultado = notificationService.enviarCorreoElectronico(request);
        Assertions.assertEquals("Notificación enviada exitosamente al usuario.", resultado);
    }

    @Test
    public void testEnviarCorreo_DestinatarioVacio_Exception() {
        NotificationRequest request = new NotificationRequest("", "Asunto", "Mensaje");

        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            notificationService.enviarCorreoElectronico(request)
        );
    }

    @Test
    public void testEnviarCorreo_DestinatarioNull_Exception() {
        NotificationRequest request = new NotificationRequest(null, "Asunto", "Mensaje");

        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            notificationService.enviarCorreoElectronico(request)
        );
    }
}