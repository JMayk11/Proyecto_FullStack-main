package com.perfulandia.microservicio.proveedores.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String contactoEmail;

    @Column(nullable = false)
    private String telefono; // <-- Campo añadido para datos de contacto completos

    @Column(nullable = false)
    private String marcaDistribuidora;

    @Column(nullable = false)
    private String tipoProveedor; // <-- "NACIONAL" o "INTERNACIONAL" según HU-11

    public Proveedor() {}

    public Proveedor(String nombre, String contactoEmail, String telefono, String marcaDistribuidora, String tipoProveedor) {
        this.nombre = nombre;
        this.contactoEmail = contactoEmail;
        this.telefono = telefono;
        this.marcaDistribuidora = marcaDistribuidora;
        this.tipoProveedor = tipoProveedor;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContactoEmail() { return contactoEmail; }
    public void setContactoEmail(String contactoEmail) { this.contactoEmail = contactoEmail; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMarcaDistribuidora() { return marcaDistribuidora; }
    public void setMarcaDistribuidora(String marcaDistribuidora) { this.marcaDistribuidora = marcaDistribuidora; }

    public String getTipoProveedor() { return tipoProveedor; }
    public void setTipoProveedor(String tipoProveedor) { this.tipoProveedor = tipoProveedor; }
}