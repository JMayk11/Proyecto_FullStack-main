package com.perfulandia.microservicio_soporte.controllers;

import com.perfulandia.microservicio_soporte.entities.Ticket;
import com.perfulandia.microservicio_soporte.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/soporte")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Endpoint para abrir un caso o ticket de soporte amarrado a un pedido.
     * POST http://localhost:8088/api/soporte/ticket/abrir
     * (Cumple con HU-25: Clasifica prioridad automática ALTA/MEDIA/BAJA)
     */
    @PostMapping("/ticket/abrir")
    public ResponseEntity<?> abrirTicket(@RequestBody Ticket ticket) {
        try {
            Ticket nuevoTicket = ticketService.abrirTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para procesar una devolución física e incrementar stock vía Feign Client.
     * PUT http://localhost:8088/api/soporte/ticket/{ticketId}/devolucion/{perfumeId}?cantidad=2
     * (Cumple con HU-26: Reingreso automático y emisión de Nota de Crédito simulada)
     */
    @PutMapping("/ticket/{ticketId}/devolucion/{perfumeId}")
    public ResponseEntity<?> procesarDevolucion(
            @PathVariable Long ticketId,
            @PathVariable Long perfumeId,
            @RequestParam int cantidad) {
        try {
            Ticket ticketProcesado = ticketService.procesarDevolucionFisica(ticketId, perfumeId, cantidad);
            return ResponseEntity.ok(ticketProcesado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para cerrar un caso de soporte y alertar al cliente.
     * PUT http://localhost:8088/api/soporte/ticket/{ticketId}/cerrar
     * (Cumple con HU-27: Transición de estado y notificación automática por mail)
     */
    @PutMapping("/ticket/{ticketId}/cerrar")
    public ResponseEntity<?> cerrarTicket(@PathVariable Long ticketId) {
        try {
            Ticket ticketCerrado = ticketService.cerrarTicket(ticketId);
            return ResponseEntity.ok(ticketCerrado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}