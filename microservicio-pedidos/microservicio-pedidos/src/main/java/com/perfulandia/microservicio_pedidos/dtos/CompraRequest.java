package com.perfulandia.microservicio_pedidos.dtos;

public class CompraRequest {
    private Long usuarioId;
    private Long productoId;
    private Integer cantidad; // 👈 NUEVO: Unidades a comprar
    private String metodoPago;

    // Métodos Getters y Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; } // 👈 Getter nuevo
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; } // 👈 Setter nuevo

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}