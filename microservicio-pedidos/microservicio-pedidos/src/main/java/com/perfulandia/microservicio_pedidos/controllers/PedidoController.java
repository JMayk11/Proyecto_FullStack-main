package com.perfulandia.microservicio_pedidos.controllers;

import com.perfulandia.microservicio_pedidos.clients.ProductoClient;
import com.perfulandia.microservicio_pedidos.clients.PagoClient;
import com.perfulandia.microservicio_pedidos.entities.Pedido;
import com.perfulandia.microservicio_pedidos.entities.ProductoModel;
import com.perfulandia.microservicio_pedidos.entities.PagoModel;
import com.perfulandia.microservicio_pedidos.dtos.CompraRequest; 
import com.perfulandia.microservicio_pedidos.repositories.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Módulo de Compras", description = "Endpoints interactivos para la gestión y procesamiento de órdenes")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private PagoClient pagoClient;

    @GetMapping("/lista")
    @Operation(summary = "Listar todos los pedidos", description = "Retorna una lista completa de los registros históricos almacenados en la base de datos local.")
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/simular-compra/{productoId}")
    @Operation(summary = "Simular creación de pedido", description = "Valida la existencia del perfume en el catálogo mediante Feign y calcula totales sin alterar la persistencia.")
    public ResponseEntity<?> simularCompra(@PathVariable Long productoId) {
        try {
            ProductoModel producto = productoClient.obtenerProductoPorId(productoId);
            
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setUsuarioId(1L); 
            nuevoPedido.setEstado("PROCESANDO_CON_FEIGN");
            nuevoPedido.setTotal(producto.getPrecio());
            
            return ResponseEntity.ok(nuevoPedido);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error: No se pudo conectar con el microservicio de productos o el producto ID " + productoId + " no existe.");
        }
    }

    /* Modificado para soportar validación y descuento de Stock dinámico */
    @PostMapping("/comprar") 
    @Operation(summary = "Ejecutar flujo de compra síncrona con validación de stock", description = "Valida y descuenta stock en Productos antes de proceder al cobro de la transacción.")
    public ResponseEntity<?> realizarCompra(@RequestBody CompraRequest solicitud) {
        try {
            // 1. Buscamos los datos del perfume para conocer su precio unitario
            ProductoModel producto = productoClient.obtenerProductoPorId(solicitud.getProductoId());

            // 2. Descontamos el stock de forma síncrona en el microservicio de Productos
            // Si no hay stock suficiente, este llamado arrojará una excepción e interrumpirá el flujo.
            productoClient.descontarStock(solicitud.getProductoId(), solicitud.getCantidad());

            // 3. Registramos la orden calculando el TOTAL REAL (Precio unitario * Cantidad elegida)
            Pedido pedido = new Pedido();
            pedido.setUsuarioId(solicitud.getUsuarioId());
            pedido.setTotal(producto.getPrecio() * solicitud.getCantidad()); 
            pedido.setEstado("PENDIENTE_DE_PAGO");
            pedido.setFecha(java.time.LocalDateTime.now());
            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            // 4. Preparamos e invocamos de forma síncrona al microservicio de Pagos
            PagoModel pago = new PagoModel();
            pago.setPedidoId(pedidoGuardado.getId());
            pago.setMonto(pedidoGuardado.getTotal());
            pago.setMetodoPago(solicitud.getMetodoPago());

            PagoModel pagoProcesado = pagoClient.procesarPago(pago);

            // 5. Dictaminamos el estado final de la compra según la respuesta transaccional de Pagos
            if ("APROBADO".equals(pagoProcesado.getEstado())) {
                pedidoGuardado.setEstado("COMPLETADO");
            } else {
                pedidoGuardado.setEstado("RECHAZADO");
            }
            
            pedidoRepository.save(pedidoGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoGuardado);

        } catch (Exception e) {
            // En caso de stock insuficiente o caída de servicios, capturamos el mensaje original de error de forma clara
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error en la transacción: No se pudo completar la compra. Detalles: " + e.getMessage());
        }
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear un pedido manualmente", description = "Inserta de manera explícita un objeto Pedido estructurado en formato JSON.")
    public Pedido crearPedido(@RequestBody Pedido pedido) {
        pedido.setFecha(java.time.LocalDateTime.now());
        if (pedido.getEstado() == null) {
            pedido.setEstado("PENDIENTE");
        }
        return pedidoRepository.save(pedido);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Filtrar historial por usuario", description = "Recupera todas las órdenes asociadas al ID de un cliente en específico.")
    public List<Pedido> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
}