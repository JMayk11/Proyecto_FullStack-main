package com.perfulandia.microservicio_soporte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients; // <-- Importar

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients 
public class MicroservicioSoporteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioSoporteApplication.class, args);
    }
}