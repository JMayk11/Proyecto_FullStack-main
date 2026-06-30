package com.perfulandia.microservicio_stock.service;

import com.perfulandia.microservicio_stock.entities.Stock;
import com.perfulandia.microservicio_stock.repositories.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Stock obtenerStockPorProducto(Long productoId) {
        return stockRepository.findByProductoId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró registro de stock para el producto ID: " + productoId));
    }

    @Override
    public Stock actualizarStock(Long productoId, Integer cantidadNueva) {
        if (cantidadNueva == null || cantidadNueva < 0) {
            throw new IllegalArgumentException("La cantidad de stock no puede ser negativa.");
        }

        Stock stock = stockRepository.findByProductoId(productoId)
                .orElse(new Stock(productoId, 0, 5));

        stock.setCantidad(cantidadNueva);
        return stockRepository.save(stock);
    }

    @Override
    public boolean verificarDisponibilidad(Long productoId, Integer cantidadRequerida) {
        if (cantidadRequerida == null || cantidadRequerida <= 0) {
            return false;
        }
        try {
            Stock stock = obtenerStockPorProducto(productoId);
            return stock.getCantidad() >= cantidadRequerida;
        } catch (IllegalArgumentException e) {
            return false; // Si no hay registro de stock, asumimos que no hay unidades disponibles
        }
    }
}