package com.perfulandia.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import java.util.Collections;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EurekaServerApplication.class);
        
        // Forzamos las propiedades directamente en el código del sistema
        app.setDefaultProperties(Collections.singletonMap("server.port", "8761"));
        System.setProperty("eureka.client.register-with-eureka", "false");
        System.setProperty("eureka.client.fetch-registry", "false");
        
        app.run(args);
    }
}