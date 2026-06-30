package com.perfulandia.microservicio.proveedores.repositories;

import com.perfulandia.microservicio.proveedores.entities.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByMarcaDistribuidora(String marcaDistribuidora);
    
    // 📌 Método para controlar la duplicidad exigida por la HU-11
    boolean existsByNombre(String nombre); 
}