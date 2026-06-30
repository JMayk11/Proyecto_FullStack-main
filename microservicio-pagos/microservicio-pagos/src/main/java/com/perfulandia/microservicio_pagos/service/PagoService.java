package com.perfulandia.microservicio_pagos.service;

import com.perfulandia.microservicio_pagos.entities.Pago;
import java.util.List;

public interface PagoService {
    Pago procesarPago(Long pedidoId, Double monto, String metodoPago);
    List<Pago> obtenerPagosPorPedido(Long pedidoId);
}