package com.perfulandia.microservicio_notificaciones.dto;

public class NotificationRequest {
    private String destinatario;
    private String asunto;
    private String mensaje;
//  Evita exponer entidades de la Base de Datos al exterior.

    // Constructor vacío
    public NotificationRequest() {}

    // Constructor lleno
    public NotificationRequest(String destinatario, String asunto, String mensaje) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    // Getters y Setters (Para que sume cobertura al usarlos)
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}