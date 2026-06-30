package com.perfulandia.microservicio.envios.service;

import com.perfulandia.microservicio.envios.entities.Envio;
import com.perfulandia.microservicio.envios.repositories.EnvioRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;

    public EnvioServiceImpl(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public Envio registrarEnvio(Long pedidoId, String region) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio para generar el despacho.");
        }
        if (region == null || region.trim().isEmpty()) {
            throw new IllegalArgumentException("La región de destino no puede estar vacía.");
        }

        String numeroTracking = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Envio nuevoEnvio = new Envio(pedidoId, numeroTracking, region, "En preparación");

        return envioRepository.save(nuevoEnvio);
    }

    @Override
    public Envio actualizarEstadoLogistico(Long envioId, String nuevoEstado) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el registro de envío con ID: " + envioId));
        
        envio.setEstadoLogistico(nuevoEstado);
        return envioRepository.save(envio);
    }

    @Override
    public Envio obtenerEnvioPorPedido(Long pedidoId) {
        return envioRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("No hay despachos asociados al pedido ID: " + pedidoId));
    }
}