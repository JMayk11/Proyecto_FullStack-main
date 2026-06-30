package com.perfulandia.microservicio.proveedores.service;

import com.perfulandia.microservicio.proveedores.entities.Proveedor;
import com.perfulandia.microservicio.proveedores.repositories.ProveedorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    @Test
    public void testRegistrarProveedor_Exitoso() {
        Proveedor proveedor = new Proveedor("Distribuidora Paris", "contacto@paris.com", "+569111111", "Chanel", "INTERNACIONAL");
        Mockito.when(proveedorRepository.existsByNombre("Distribuidora Paris")).thenReturn(false);
        Mockito.when(proveedorRepository.save(Mockito.any(Proveedor.class))).thenReturn(proveedor);

        Proveedor resultado = proveedorService.registrarProveedor(proveedor);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Distribuidora Paris", resultado.getNombre());
    }

    @Test
    public void testRegistrarProveedor_NombreVacio_Exception() {
        Proveedor p = new Proveedor("", "test@test.com", "1234", "Marca", "NACIONAL");
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(p));
        
        Proveedor pNull = new Proveedor(null, "test@test.com", "1234", "Marca", "NACIONAL");
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(pNull));
    }

    @Test
    public void testRegistrarProveedor_EmailInvalido_Exception() {
        Proveedor p = new Proveedor("Prov Test", "emailSinArroba.com", "1234", "Marca", "NACIONAL");
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(p));
        
        Proveedor pNull = new Proveedor("Prov Test", null, "1234", "Marca", "NACIONAL");
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(pNull));
    }

    @Test
    public void testRegistrarProveedor_TelefonoVacio_Exception() {
        Proveedor p = new Proveedor("Prov Test", "test@test.com", " ", "Marca", "NACIONAL");
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(p));
        
        Proveedor pNull = new Proveedor("Prov Test", "test@test.com", null, "Marca", "NACIONAL");
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(pNull));
    }

    @Test
    public void testRegistrarProveedor_DuplicadoHU11_Exception() {
        Proveedor p = new Proveedor("Duplicado", "test@test.com", "1234", "Marca", "NACIONAL");
        Mockito.when(proveedorRepository.existsByNombre("Duplicado")).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(p));
    }

    @Test
    public void testRegistrarProveedor_TipoInvalido_Exception() {
        Proveedor p = new Proveedor("Prov Test", "test@test.com", "1234", "Marca", "LOCAL_FALSO");
        Mockito.when(proveedorRepository.existsByNombre("Prov Test")).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(p));
        
        Proveedor pNull = new Proveedor("Prov Test", "test@test.com", "1234", "Marca", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.registrarProveedor(pNull));
    }

    @Test
    public void testObtenerProveedorPorMarca_Exitoso() {
        Proveedor proveedor = new Proveedor("Prov Chanel", "chanel@test.com", "1234", "Chanel", "INTERNACIONAL");
        Mockito.when(proveedorRepository.findByMarcaDistribuidora("Chanel")).thenReturn(Optional.of(proveedor));

        Proveedor resultado = proveedorService.obtenerProveedorPorMarca("Chanel");
        Assertions.assertEquals("Prov Chanel", resultado.getNombre());
    }

    @Test
    public void testObtenerProveedorPorMarca_Vacio_Exception() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.obtenerProveedorPorMarca(" "));
        Assertions.assertThrows(IllegalArgumentException.class, () -> proveedorService.obtenerProveedorPorMarca(null));
    }

    // 📌 COBERTURA DE LA ENTIDAD PROVEEDOR
    @Test
    public void testProveedorEntity_GettersAndSetters() {
        Proveedor p = new Proveedor();
        p.setId(5L);
        p.setNombre("Loreal Chile");
        p.setContactoEmail("contacto@loreal.cl");
        p.setTelefono("+56223456");
        p.setMarcaDistribuidora("Loreal");
        p.setTipoProveedor("NACIONAL");

        Assertions.assertEquals(5L, p.getId());
        Assertions.assertEquals("Loreal Chile", p.getNombre());
        Assertions.assertEquals("contacto@loreal.cl", p.getContactoEmail());
        Assertions.assertEquals("+56223456", p.getTelefono());
        Assertions.assertEquals("Loreal", p.getMarcaDistribuidora());
        Assertions.assertEquals("NACIONAL", p.getTipoProveedor());
    }
}