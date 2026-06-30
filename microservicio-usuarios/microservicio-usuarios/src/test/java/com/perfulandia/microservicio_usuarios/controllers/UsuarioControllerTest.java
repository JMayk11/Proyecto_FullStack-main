package com.perfulandia.microservicio_usuarios.controllers;

import com.perfulandia.microservicio_usuarios.entities.Usuario;
import com.perfulandia.microservicio_usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void testListarUsuarios_Exitoso() throws Exception {
        Usuario u1 = new Usuario("Mayko", "mayko@test.com");
        u1.setRut("19.234.567-8");
        Usuario u2 = new Usuario("Ana Gomez", "ana@test.com");
        u2.setRut("18.345.678-K");

        List<Usuario> listaMock = Arrays.asList(u1, u2);
        Mockito.when(usuarioService.listarUsuarios()).thenReturn(listaMock);

        mockMvc.perform(get("/usuarios/lista")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mayko"))
                .andExpect(jsonPath("$[1].nombre").value("Ana Gomez"));
    }

    @Test
    public void testRegistrarUsuario_Exitoso() throws Exception {
        Usuario usuarioInput = new Usuario("Carlos", "carlos@test.com");
        usuarioInput.setRut("19.234.567-8");

        Mockito.when(usuarioService.registrarUsuario(Mockito.any(Usuario.class))).thenReturn(usuarioInput);

        String jsonBody = "{\"nombre\":\"Carlos\",\"email\":\"carlos@test.com\",\"rut\":\"19.234.567-8\"}";

        mockMvc.perform(post("/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    public void testRegistrarUsuario_RutInvalido() throws Exception {
        Mockito.when(usuarioService.registrarUsuario(Mockito.any(Usuario.class)))
               .thenThrow(new IllegalArgumentException("El RUT ingresado no es válido en territorio chileno."));

        String jsonBody = "{\"nombre\":\"Carlos\",\"email\":\"carlos@test.com\",\"rut\":\"11.111.111-1\"}";

        mockMvc.perform(post("/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isBadRequest());
    }
}