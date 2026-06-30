package com.perfulandia.microservicio.proveedores.service;

import com.perfulandia.microservicio.proveedores.entities.Proveedor;

public interface ProveedorService {
    Proveedor registrarProveedor(Proveedor proveedor);
    Proveedor obtenerProveedorPorMarca(String marca);
}