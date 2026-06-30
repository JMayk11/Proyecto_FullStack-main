package com.perfulandia.microservicio_productos.repositories;

import com.perfulandia.microservicio_productos.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Aquí heredamos todos los métodos básicos: findAll(), findById(), save(), etc.
}