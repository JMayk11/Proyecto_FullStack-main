package com.perfulandia.microservicio_productos.service;

import com.perfulandia.microservicio_productos.entities.Producto;
import com.perfulandia.microservicio_productos.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        // Validación técnica influyente: Controlar fallos de ID inexistentes
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + id + " no existe en el catálogo."));
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        // Control de fraudes o errores de digitación
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor a cero.");
        }
        return productoRepository.save(producto);
    }
}