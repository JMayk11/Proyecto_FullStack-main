package com.perfulandia.microservicio_productos.service;

import com.perfulandia.microservicio_productos.config.DataInitializer;
import com.perfulandia.microservicio_productos.entities.Producto;
import com.perfulandia.microservicio_productos.repositories.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerTodosLosProductos() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto());
        Mockito.when(productoRepository.findAll()).thenReturn(lista);

        List<Producto> resultado = productoService.obtenerTodosLosProductos();
        Assertions.assertFalse(resultado.isEmpty());
    }

    @Test
    public void testObtenerProductoPorId_Exitoso() {
        Producto producto = new Producto("One Million", "Paco Rabanne", 95.0, 10, "Intenso");
        Mockito.when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerProductoPorId(1L);
        Assertions.assertEquals("One Million", resultado.getNombre());
    }

    @Test
    public void testObtenerProductoPorId_Inexistente_Exception() {
        Mockito.when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> 
            productoService.obtenerProductoPorId(99L)
        );
    }

    @Test
    public void testGuardarProducto_Exitoso() {
        Producto producto = new Producto("Le Male", "JPG", 110.0, 5, "Moderno");
        Mockito.when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.guardarProducto(producto);
        Assertions.assertNotNull(resultado);
    }

    @Test
    public void testGuardarProducto_PrecioInvalido_Exception() {
        Producto p1 = new Producto("Test", "Marca", null, 5, "Desc");
        Producto p2 = new Producto("Test", "Marca", -10.0, 5, "Desc");

        // Restauradas las dos validaciones completas que se habían roto
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productoService.guardarProducto(p1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productoService.guardarProducto(p2);
        });
    }

    // 📌 COBERTURA DEL INITIALIZER (DataInitializer)
    @Test
    public void testDataInitializer_CuandoEstaVacio() throws Exception {
        Mockito.when(productoRepository.count()).thenReturn(0L);
        
        DataInitializer initializer = new DataInitializer();
        
        // Obtenemos el método mediante reflexión e ignoramos las restricciones de visibilidad de paquete
        Method method = initializer.getClass().getDeclaredMethod("initDatabase", ProductoRepository.class);
        method.setAccessible(true);
        
        CommandLineRunner runner = (CommandLineRunner) method.invoke(initializer, productoRepository);

        if (runner != null) {
            runner.run(new String[]{});
            // Verificamos que al menos se llamó a guardar un producto premium
            Mockito.verify(productoRepository, Mockito.atLeastOnce()).save(Mockito.any(Producto.class));
        }
    }

    @Test
    public void testDataInitializer_CuandoYaTieneDatos() throws Exception {
        Mockito.when(productoRepository.count()).thenReturn(5L);
        
        DataInitializer initializer = new DataInitializer();
        
        // Obtenemos el método mediante reflexión e ignoramos las restricciones de visibilidad de paquete
        Method method = initializer.getClass().getDeclaredMethod("initDatabase", ProductoRepository.class);
        method.setAccessible(true);
        
        CommandLineRunner runner = (CommandLineRunner) method.invoke(initializer, productoRepository);

        if (runner != null) {
            runner.run(new String[]{});
            // Si ya hay datos, no debe intentar guardar nada nuevo
            Mockito.verify(productoRepository, Mockito.never()).save(Mockito.any(Producto.class));
        }
    }

    // 📌 COBERTURA DE ENTIDAD (Getters, Setters y constructores huérfanos)
    @Test
    public void testProductoEntity_SettersYGetters() {
        Producto producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Jadore");
        producto.setMarca("Dior");
        producto.setPrecio(150.0);
        producto.setStock(20);
        producto.setDescription("Floral");

        Assertions.assertEquals(10L, producto.getId());
        Assertions.assertEquals("Jadore", producto.getNombre());
        Assertions.assertEquals("Dior", producto.getMarca());
        Assertions.assertEquals(150.0, producto.getPrecio());
        Assertions.assertEquals(20, producto.getStock());
        Assertions.assertEquals("Floral", producto.getDescription());
    }
}