package com.perfulandia.microservicio_pedidos.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Almacena el ID del usuario dueño del pedido (asociación lógica)
    private Long usuarioId; 
    
    private LocalDateTime fecha;
    private String estado; // "PENDIENTE", "PAGADO", "CANCELADO"
    private Double total;

    // Constructor vacío obligatorio para JPA
    public Pedido() {}

    public Pedido(Long usuarioId, String estado, Double total) {
        this.usuarioId = usuarioId;
        this.fecha = LocalDateTime.now();
        this.estado = estado;
        this.total = total;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}