package com.perfulandia.microservicio_soporte.service;

import com.perfulandia.microservicio_soporte.entities.Ticket;

public interface TicketService {
    Ticket abrirTicket(Ticket ticket);
    Ticket procesarDevolucionFisica(Long ticketId, Long perfumeId, int cantidad);
    Ticket cerrarTicket(Long ticketId);
}