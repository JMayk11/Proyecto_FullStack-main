package com.perfulandia.microservicio.envios.repositories;

import com.perfulandia.microservicio.envios.entities.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    Optional<Envio> findByNumeroTracking(String numeroTracking);
    List<Envio> findByRegionAndEstadoLogistico(String region, String estadoLogistico);
    
    // Spring interpreta el nombre en tiempo de compilación y autogenera el SQL, blindando el sistema contra SQL Injection
    Optional<Envio> findByPedidoId(Long pedidoId); 
}