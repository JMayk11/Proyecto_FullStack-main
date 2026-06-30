package com.perfulandia.microservicio.proveedores.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.microservicio.proveedores.entities.Proveedor;
import com.perfulandia.microservicio.proveedores.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProveedorController.class)
public class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegistrarProveedor_Exitoso() throws Exception {
        Proveedor proveedor = new Proveedor("Distribuidora Paris", "contacto@paris.com", "+569111111", "Chanel", "INTERNACIONAL");
        
        Mockito.when(proveedorService.registrarProveedor(Mockito.any(Proveedor.class))).thenReturn(proveedor);

        mockMvc.perform(post("/api/proveedores/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Distribuidora Paris"));
    }

    @Test
    public void testRegistrarProveedor_BadRequest_Exception() throws Exception {
        Proveedor proveedorInvalido = new Proveedor("", "contacto@paris.com", "+569111111", "Chanel", "INTERNACIONAL");
        
        Mockito.when(proveedorService.registrarProveedor(Mockito.any(Proveedor.class)))
               .thenThrow(new IllegalArgumentException("El nombre del proveedor es obligatorio."));

        mockMvc.perform(post("/api/proveedores/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proveedorInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testObtenerPorMarca_Exitoso() throws Exception {
        Proveedor proveedor = new Proveedor("Distribuidora Paris", "contacto@paris.com", "+569111111", "Chanel", "INTERNACIONAL");
        
        Mockito.when(proveedorService.obtenerProveedorPorMarca("Chanel")).thenReturn(proveedor);

        mockMvc.perform(get("/api/proveedores/marca/Chanel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marcaDistribuidora").value("Chanel"));
    }

    @Test
    public void testObtenerPorMarca_NotFound_Exception() throws Exception {
        Mockito.when(proveedorService.obtenerProveedorPorMarca("Inexistente"))
               .thenThrow(new IllegalArgumentException("No se encontró ningún proveedor asociado a la marca: Inexistente"));

        mockMvc.perform(get("/api/proveedores/marca/Inexistente"))
                .andExpect(status().isNotFound());
    }
}