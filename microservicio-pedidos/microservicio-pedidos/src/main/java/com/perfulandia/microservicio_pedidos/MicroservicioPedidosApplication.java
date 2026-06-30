package com.perfulandia.microservicio_pedidos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients; 

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients 
@OpenAPIDefinition(
    info = @Info(
        title = "API de Perfumelandia 🧪✨",
        version = "1.0",
        description = "Core centralizado de microservicios para la gestión automatizada de productos, órdenes de compra y pasarela síncrona de pagos."
    )
)
public class MicroservicioPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioPedidosApplication.class, args);
    }
}