package com.perfulandia.microservicio_pedidos.service;

import com.perfulandia.microservicio_pedidos.entities.Pedido;
import com.perfulandia.microservicio_pedidos.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido crearPedido(Long usuarioId, Double total) {
        if (total == null || total <= 0) {
            throw new IllegalArgumentException("El monto total del pedido debe ser mayor a cero.");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio.");
        }

        // Usamos el constructor exacto de tu clase Pedido
        Pedido pedido = new Pedido(usuarioId, "PENDIENTE", total);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido actualizarEstado(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el pedido con ID: " + pedidoId));
        
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido cancelarPedidoPorInactividad(Long pedidoId) {
        return actualizarEstado(pedidoId, "CANCELADO");
    }
}