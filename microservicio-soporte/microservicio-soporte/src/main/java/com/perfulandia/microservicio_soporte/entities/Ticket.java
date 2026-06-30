package com.perfulandia.microservicio_soporte.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId; // Requisito estricto HU-25 (Amarre a un pedido previo)

    @Column(nullable = false)
    private String descripcionInconveniente;

    @Column(nullable = false)
    private String tipoUrgencia; // Ej: "ERROR_DESPACHO", "CAMBIO_FRAGANCIA", "DUDA"

    @Column(nullable = false)
    private String prioridad; // Calculada automáticamente: ALTA, MEDIA, BAJA

    @Column(nullable = false)
    private String estado; // ABIERTO, EN_PROCESO, CERRADO

    public Ticket() {}

    public Ticket(Long pedidoId, String descripcionInconveniente, String tipoUrgencia, String prioridad, String estado) {
        this.pedidoId = pedidoId;
        this.descripcionInconveniente = descripcionInconveniente;
        this.tipoUrgencia = tipoUrgencia;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getDescripcionInconveniente() { return descripcionInconveniente; }
    public void setDescripcionInconveniente(String descripcionInconveniente) { this.descripcionInconveniente = descripcionInconveniente; }

    public String getTipoUrgencia() { return tipoUrgencia; }
    public void setTipoUrgencia(String tipoUrgencia) { this.tipoUrgencia = tipoUrgencia; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}