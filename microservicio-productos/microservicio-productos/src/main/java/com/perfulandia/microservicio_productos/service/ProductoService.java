package com.perfulandia.microservicio_productos.service;

import com.perfulandia.microservicio_productos.entities.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> obtenerTodosLosProductos();
    Producto obtenerProductoPorId(Long id);
    Producto guardarProducto(Producto producto);
}