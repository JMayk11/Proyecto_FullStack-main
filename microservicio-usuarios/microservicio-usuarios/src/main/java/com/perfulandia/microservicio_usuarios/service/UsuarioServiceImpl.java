package com.perfulandia.microservicio_usuarios.service;

import com.perfulandia.microservicio_usuarios.entities.Usuario;
import com.perfulandia.microservicio_usuarios.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List; // Import para manejar listas

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

  
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
        // Permite el dígito "K" e ignora caracteres especiales que rompen un tipo numérico puro
        rut = rut.replace(".", "").replace("-", "").trim().toUpperCase();
        if (rut.length() < 2) return false;

        try {
            // Descomposición: Aísla posicionalmente el último carácter como el Dígito Verificador existente (dvExistente)
            // Extrae el resto de la cadena como la subcadena numérica ejecutable (cuerpoNum).
            char dvExistente = rut.charAt(rut.length() - 1);
            String cuerpoNum = rut.substring(0, rut.length() - 1);
            
            int rutNum = Integer.parseInt(cuerpoNum);
            int suma = 0;
            int multiplicador = 2;
            
            // Ponderación cíclica: Mediante un ciclo while y aritmética modular (rutNum % 10).
            // Extrae los dígitos de derecha a izquierda (desde las unidades hacia arriba).
            while (rutNum > 0) {
                suma += (rutNum % 10) * multiplicador;
                rutNum /= 10;
                multiplicador++;
                if (multiplicador > 7) {
                    multiplicador = 2;
                }
            }
            
            // Se calcula el residuo aritmético base 11 y se resta al factor 11.
            int resto = suma % 11;
            int resultadoDv = 11 - resto;
            
            char dvCalculado;
            if (resultadoDv == 11) {
                dvCalculado = '0';
            } else if (resultadoDv == 10) {
                dvCalculado = 'K';
            } else {
                dvCalculado = Character.forDigit(resultadoDv, 10); //Si da 11 mapea a '0'; si da 10 mapea a 'K'
            }

            return dvExistente == dvCalculado;
            
        } catch (Exception e) {
            return false;
        }
    }
}