package com.perfulandia.microservicio_pagos.service;

import com.perfulandia.microservicio_pagos.entities.Pago;
import com.perfulandia.microservicio_pagos.repositories.PagoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public Pago procesarPago(Long pedidoId, Double monto, String metodoPago) {
        // Regla de Negocio 1: Bloqueo de montos corruptos o negativos
        if (monto == null || monto <= 0) {
            throw new IllegalArgumentException("El monto a facturar debe ser estrictamente mayor a cero.");
        }
        
        // Regla de Negocio 2: Validar que se especifique un canal de pago
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago es obligatorio para procesar la transacción.");
        }

        if (pedidoId == null) {
            throw new IllegalArgumentException("El ID del pedido asociado es obligatorio.");
        }

        // Construcción limpia usando setters debido a que tu entidad cuenta con constructor vacío
        Pago nuevoPago = new Pago();
        nuevoPago.setPedidoId(pedidoId);
        nuevoPago.setMonto(monto);
        nuevoPago.setMetodoPago(metodoPago.toUpperCase());
        
        // Simulamos la pasarela de pagos aprobando la transacción por defecto
        nuevoPago.setEstado("APROBADO");

        return pagoRepository.save(nuevoPago);
    }
    // Si la base de datos de otro servicio se corrompe, la persistencia transaccional de Pagos queda totalmente aislada y blindada.
    
    @Override
    public List<Pago> obtenerPagosPorPedido(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId);
    }
}