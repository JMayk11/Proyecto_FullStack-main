package com.perfulandia.microservicio.envios.service;

import com.perfulandia.microservicio.envios.entities.Envio;

public interface EnvioService {
    Envio registrarEnvio(Long pedidoId, String region);
    Envio actualizarEstadoLogistico(Long envioId, String nuevoEstado);
    Envio obtenerEnvioPorPedido(Long pedidoId);
}