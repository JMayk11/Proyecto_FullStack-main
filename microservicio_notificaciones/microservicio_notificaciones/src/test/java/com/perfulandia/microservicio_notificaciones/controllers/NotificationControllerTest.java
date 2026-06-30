package com.perfulandia.microservicio_notificaciones.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.microservicio_notificaciones.dto.NotificationRequest;
import com.perfulandia.microservicio_notificaciones.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testEnviarCorreoElectronico_Exitoso() throws Exception {
        NotificationRequest requestBody = new NotificationRequest(
                "cliente@perfulandia.com", 
                "Test de Notificación", 
                "Este es un mensaje de prueba."
        );

        Mockito.when(notificationService.enviarCorreoElectronico(Mockito.any(NotificationRequest.class)))
               .thenReturn("Notificación enviada exitosamente al usuario.");

        mockMvc.perform(post("/api/notificaciones/enviar-correo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación enviada exitosamente al usuario."));
    }

    @Test
    public void testEnviarCorreoElectronico_DestinatarioVacio_BadRequest() throws Exception {
        NotificationRequest requestBody = new NotificationRequest(
                "", 
                "Test Fallido", 
                "No debería enviarse."
        );

        Mockito.when(notificationService.enviarCorreoElectronico(Mockito.any(NotificationRequest.class)))
               .thenThrow(new IllegalArgumentException("Error: El destinatario no puede estar vacío."));

        mockMvc.perform(post("/api/notificaciones/enviar-correo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: El destinatario no puede estar vacío."));
    }

    @Test
    public void testEnviarCorreoElectronico_DestinatarioNull_BadRequest() throws Exception {
        NotificationRequest requestBody = new NotificationRequest(
                null, 
                "Test Fallido", 
                "No debería enviarse."
        );

        Mockito.when(notificationService.enviarCorreoElectronico(Mockito.any(NotificationRequest.class)))
               .thenThrow(new IllegalArgumentException("Error: El destinatario no puede estar vacío."));

        mockMvc.perform(post("/api/notificaciones/enviar-correo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: El destinatario no puede estar vacío."));
    }
}