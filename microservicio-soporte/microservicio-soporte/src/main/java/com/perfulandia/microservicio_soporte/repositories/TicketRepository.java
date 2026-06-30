package com.perfulandia.microservicio_soporte.repositories;

import com.perfulandia.microservicio_soporte.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByPedidoId(Long pedidoId);
}