package com.perfulandia.microservicio.proveedores.service;

import com.perfulandia.microservicio.proveedores.entities.Proveedor;
import com.perfulandia.microservicio.proveedores.repositories.ProveedorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public Proveedor registrarProveedor(Proveedor proveedor) {
        // Validaciones base de campos vacíos
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor es obligatorio.");
        }
        if (proveedor.getContactoEmail() == null || !proveedor.getContactoEmail().contains("@")) {
            throw new IllegalArgumentException("El correo electrónico de contacto no es válido.");
        }
        if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono de contacto es obligatorio.");
        }
        
        // 📌 CRITERIO HU-11: Evitar registros duplicados
        if (proveedorRepository.existsByNombre(proveedor.getNombre())) {
            throw new IllegalArgumentException("Error: Ya existe un proveedor registrado con el nombre: " + proveedor.getNombre());
        }

        // Validar tipo de procedencia solicitado por la HU
        if (proveedor.getTipoProveedor() == null || 
            (!proveedor.getTipoProveedor().equalsIgnoreCase("NACIONAL") && !proveedor.getTipoProveedor().equalsIgnoreCase("INTERNACIONAL"))) {
            throw new IllegalArgumentException("El tipo de proveedor debe ser NACIONAL o INTERNACIONAL.");
        }

        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor obtenerProveedorPorMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca a buscar no puede estar vacío.");
        }
        return proveedorRepository.findByMarcaDistribuidora(marca)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ningún proveedor asociado a la marca: " + marca));
    }
}