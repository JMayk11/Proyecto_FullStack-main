package com.perfulandia.microservicio_usuarios.service;

import com.perfulandia.microservicio_usuarios.entities.Usuario;
import com.perfulandia.microservicio_usuarios.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List; // 👈 Agregamos el import para manejar listas

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // 👈 AGREGA ESTE MÉTODO: Implementación para listar usuarios delegando al repositorio
    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // Si el RUT es inválido, lanzamos el error esperado por la prueba
        if (!validarRutChileno(usuario.getRut())) {
            throw new IllegalArgumentException("El RUT ingresado no es válido en territorio chileno.");
        }
        return usuarioRepository.save(usuario);
    }

    public boolean validarRutChileno(String rut) {
        if (rut == null) return false;
        
        // Limpiar puntos, guiones y espacios, pasar a mayúsculas
        rut = rut.replace(".", "").replace("-", "").trim().toUpperCase();
        if (rut.length() < 2) return false;

        try {
            char dvExistente = rut.charAt(rut.length() - 1);
            String cuerpoNum = rut.substring(0, rut.length() - 1);
            
            int rutNum = Integer.parseInt(cuerpoNum);
            int suma = 0;
            int multiplicador = 2;
            
            while (rutNum > 0) {
                suma += (rutNum % 10) * multiplicador;
                rutNum /= 10;
                multiplicador++;
                if (multiplicador > 7) {
                    multiplicador = 2;
                }
            }
            
            int resto = suma % 11;
            int resultadoDv = 11 - resto;
            
            char dvCalculado;
            if (resultadoDv == 11) {
                dvCalculado = '0';
            } else if (resultadoDv == 10) {
                dvCalculado = 'K';
            } else {
                dvCalculado = Character.forDigit(resultadoDv, 10);
            }

            return dvExistente == dvCalculado;
            
        } catch (Exception e) {
            return false;
        }
    }
}