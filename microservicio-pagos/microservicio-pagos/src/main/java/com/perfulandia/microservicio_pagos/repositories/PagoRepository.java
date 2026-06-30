package com.perfulandia.microservicio_pagos.repositories;

import com.perfulandia.microservicio_pagos.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    // Un método personalizado útil por si queremos buscar todos los pagos de un pedido específico
    List<Pago> findByPedidoId(Long pedidoId);
}