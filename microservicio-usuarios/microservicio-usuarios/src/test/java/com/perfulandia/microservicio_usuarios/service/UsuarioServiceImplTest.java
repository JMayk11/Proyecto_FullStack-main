package com.perfulandia.microservicio_usuarios.service;

import com.perfulandia.microservicio_usuarios.entities.Usuario;
import com.perfulandia.microservicio_usuarios.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    public void testListarUsuarios_Service_Exitoso() {
        Usuario u = new Usuario("Mayko", "mayko@test.com");
        Mockito.when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u));

        List<Usuario> resultado = usuarioService.listarUsuarios();
        
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
    }

    @Test
    public void testRegistrarUsuario_RutValidoNumero_Exitoso() {
        Usuario usuario = new Usuario("Mayko", "mayko@test.com");
        // 12.345.678-5 cumple perfectamente con la matemática de tu método while
        usuario.setRut("12.345.678-5"); 

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.registrarUsuario(usuario);
        
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("12.345.678-5", resultado.getRut());
    }

    @Test
    public void testRegistrarUsuario_RutValidoK_Exitoso() {
        Usuario usuario = new Usuario("Ana Gomez", "ana@test.com");
        // 12.345.670-K genera un resultadoDv de 10, activando tu bloque dvCalculado = 'K'
        usuario.setRut("12.345.670-K"); 

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.registrarUsuario(usuario);
        
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("12.345.670-K", resultado.getRut());
    }

    @Test
    public void testRegistrarUsuario_RutInvalido_Exception() {
        Usuario usuario = new Usuario("Invalido", "test@test.com");
        // Usamos el mismo cuerpo pero con DV erróneo (9) para gatillar el IllegalArgumentException
        usuario.setRut("12.345.678-9"); 

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrarUsuario(usuario);
        });
    }

    @Test
    public void testValidarRutChileno_CasosBordeYErrores() {
        Assertions.assertFalse(usuarioService.validarRutChileno(null));
        Assertions.assertFalse(usuarioService.validarRutChileno(" "));
        Assertions.assertFalse(usuarioService.validarRutChileno("1"));
        Assertions.assertFalse(usuarioService.validarRutChileno("A.BBB.CCC-D"));
    }

    @Test
    public void testUsuarioEntity_GettersAndSetters() {
        Usuario u = new Usuario();
        u.setId(99L);
        u.setNombre("Carlos");
        u.setEmail("carlos@test.com");
        u.setRut("12.345.678-5");

        Assertions.assertEquals(99L, u.getId());
        Assertions.assertEquals("Carlos", u.getNombre());
        Assertions.assertEquals("carlos@test.com", u.getEmail());
        Assertions.assertEquals("12.345.678-5", u.getRut());
    }
}