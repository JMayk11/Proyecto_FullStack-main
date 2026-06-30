package com.perfulandia.microservicio_usuarios.controllers;

import com.perfulandia.microservicio_usuarios.entities.Usuario;
import com.perfulandia.microservicio_usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // El controlador ahora solo interactúa con el Servicio (Patrón CSR)

    @GetMapping("/lista")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios(); // 👈 CAMBIADO: Ahora delega correctamente la lógica
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}