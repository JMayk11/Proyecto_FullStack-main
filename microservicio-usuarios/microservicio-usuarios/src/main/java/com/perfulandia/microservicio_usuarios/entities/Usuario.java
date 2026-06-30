package com.perfulandia.microservicio_usuarios.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String email;
    private String rut; // 1. Agregamos la variable que faltaba para la HU-01

    // Constructor vacío obligatorio para JPA (Se mantiene igual)
    public Usuario() {}

    // Constructor rápido para llenar datos (Se mantiene igual)
    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    // Getters y Setters existentes (Se mantienen intactos)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // 2. Agregamos los Getters y Setters específicos para el RUT
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
}