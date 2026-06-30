package com.perfulandia.microservicio.envios.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Delegamiento de la autoincrementación eficiente al motor.
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false, unique = true) // Garantizar integridad referencial a nivel de esquema de base de datos.
    private String numeroTracking;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String estadoLogistico; // "En preparación", "Despachado", "En Ruta", "Entregado"

    public Envio() {}

    public Envio(Long pedidoId, String numeroTracking, String region, String estadoLogistico) {
        this.pedidoId = pedidoId;
        this.numeroTracking = numeroTracking;
        this.region = region;
        this.estadoLogistico = estadoLogistico;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getNumeroTracking() { return numeroTracking; }
    public void setNumeroTracking(String numeroTracking) { this.numeroTracking = numeroTracking; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getEstadoLogistico() { return estadoLogistico; }
    public void setEstadoLogistico(String estadoLogistico) { this.estadoLogistico = estadoLogistico; }
}