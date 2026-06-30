package com.perfulandia.microservicio_pagos.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El ID del pedido que viene del microservicio-pedidos
    private Long pedidoId; 
    
    private Double monto;
    
    // Ej: "TARJETA_CREDITO", "PAYPAL", "TRANSFERENCIA"
    private String metodoPago; 
    
    // Ej: "APROBADO", "RECHAZADO", "PENDIENTE"
    private String estado;

    // Constructores
    public Pago() {
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}