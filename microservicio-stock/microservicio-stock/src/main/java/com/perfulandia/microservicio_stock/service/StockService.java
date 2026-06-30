package com.perfulandia.microservicio_stock.service;

import com.perfulandia.microservicio_stock.entities.Stock;

public interface StockService {
    Stock obtenerStockPorProducto(Long productoId);
    Stock actualizarStock(Long productoId, Integer cantidadNueva);
    boolean verificarDisponibilidad(Long productoId, Integer cantidadRequerida); // <-- El método perdido
}