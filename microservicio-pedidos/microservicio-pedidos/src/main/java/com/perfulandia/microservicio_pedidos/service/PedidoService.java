package com.perfulandia.microservicio_pedidos.service;

import com.perfulandia.microservicio_pedidos.entities.Pedido;

public interface PedidoService {
    Pedido crearPedido(Long usuarioId, Double total);
    Pedido actualizarEstado(Long pedidoId, String nuevoEstado);
    Pedido cancelarPedidoPorInactividad(Long pedidoId);
}