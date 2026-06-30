package com.perfulandia.microservicio_soporte.service;

import com.perfulandia.microservicio_soporte.entities.Ticket;
import com.perfulandia.microservicio_soporte.repositories.TicketRepository;
import com.perfulandia.microservicio_soporte.clients.InventarioClient;
import com.perfulandia.microservicio_soporte.clients.NotificacionesClient; // <-- AGREGA ESTE IMPORT
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final InventarioClient inventarioClient; 
    private final NotificacionesClient notificacionesClient; // <-- 1. DECLARAR EL CLIENTE DE NOTIFICACIONES

    // 2. AGREGARLO AL CONSTRUCTOR (Se inyecta automáticamente por Spring)
    public TicketServiceImpl(TicketRepository ticketRepository, InventarioClient inventarioClient, NotificacionesClient notificacionesClient) {
        this.ticketRepository = ticketRepository;
        this.inventarioClient = inventarioClient;
        this.notificacionesClient = notificacionesClient;
    }

    @Override
    public Ticket abrirTicket(Ticket ticket) {
        // 📌 CRITERIO HU-25: Validar que el ticket venga amarrado a un ID de pedido previo
        if (ticket.getPedidoId() == null || ticket.getPedidoId() <= 0) {
            throw new IllegalArgumentException("Error: Para solicitar soporte debe ingresar un ID de pedido válido.");
        }

        if (ticket.getTipoUrgencia() == null || ticket.getTipoUrgencia().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de urgencia es obligatorio.");
        }

        // 📌 CRITERIO HU-25: Asignación automática de prioridad basada en el caso
        String urgencia = ticket.getTipoUrgencia().toUpperCase();
        if (urgencia.equals("ERROR_DESPACHO") || urgencia.equals("PRODUCTO_DAÑADO")) {
            ticket.setPrioridad("ALTA");
        } else if (urgencia.equals("CAMBIO_FRAGANCIA")) {
            ticket.setPrioridad("MEDIA");
        } else {
            ticket.setPrioridad("BAJA");
        }

        // Estado inicial obligatorio al abrir un caso
        ticket.setEstado("ABIERTO");

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket procesarDevolucionFisica(Long ticketId, Long perfumeId, int cantidad) {
        // Buscamos el ticket en la base de datos
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el ticket con ID: " + ticketId));

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reingresar al inventario debe ser mayor a cero.");
        }

        // Transicionamos el estado a en proceso
        ticket.setEstado("EN_PROCESO");

        // 📌 CRITERIO HU-26: Comunicación remota síncrona vía Feign con el MS de Inventario/Stock
        // Reingresa el perfume apto de forma automática al stock físico local de la tienda
        inventarioClient.reingresarStockFisico(perfumeId, cantidad);

        // Imprimimos una simulación de emisión de nota de crédito exigida por la HU-26
        System.out.println("[SII] NOTA DE CRÉDITO EMITIDA EXITOSAMENTE ASOCIADA AL TICKET ID: " + ticketId);

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket cerrarTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el ticket con ID: " + ticketId));

        // Transicionamos el estado a Cerrado
        ticket.setEstado("CERRADO");

        // 📌 CRITERIO HU-27: Empaquetar datos para enviarlos por red al MS Notificaciones
        Map<String, String> datosCorreo = new HashMap<>();
        datosCorreo.put("destinatario", "cliente_pedido_" + ticket.getPedidoId() + "@correo.com");
        datosCorreo.put("asunto", "Tu Ticket de Soporte #" + ticketId + " ha sido Cerrado");
        datosCorreo.put("mensaje", "Estimado cliente, su caso ha sido RESUELTO por nuestro equipo técnico.");

        try {
            // 🔥 Llamada remota síncrona vía OpenFeign
            notificacionesClient.enviarCorreo(datosCorreo);
            System.out.println("[SOPORTE] Orden de notificación enviada exitosamente al MS Notificaciones.");
        } catch (Exception e) {
            // Un try-catch preventivo para que si Notificaciones está apagado, el ticket igual se guarde cerrado
            System.out.println("[ALERTA] No se pudo conectar con el MS de Notificaciones, pero el ticket fue cerrado localmente.");
        }

        return ticketRepository.save(ticket);
    }
}