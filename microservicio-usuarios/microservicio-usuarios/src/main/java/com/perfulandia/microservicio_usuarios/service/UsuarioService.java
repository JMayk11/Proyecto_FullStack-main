package com.perfulandia.microservicio_usuarios.service;

import com.perfulandia.microservicio_usuarios.entities.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);


    List<Usuario> listarUsuarios();
}