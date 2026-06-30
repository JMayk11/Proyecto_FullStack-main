package com.perfulandia.microservicio_pedidos.repositories;

import com.perfulandia.microservicio_pedidos.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Busca automáticamente todos los pedidos asociados a un cliente específico
    List<Pedido> findByUsuarioId(Long usuarioId);
}