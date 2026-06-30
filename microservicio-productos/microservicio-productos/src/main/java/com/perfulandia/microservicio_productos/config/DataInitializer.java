package com.perfulandia.microservicio_productos.config;

import com.perfulandia.microservicio_productos.entities.Producto;
import com.perfulandia.microservicio_productos.repositories.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductoRepository repository) {
        return args -> {
            // Validamos si la base de datos ya tiene registros para no duplicarlos
            if (repository.count() == 0) {
                
                // Catálogo de Perfumes Masculinos
                repository.save(new Producto("Bleu de Chanel", "Chanel", 140.00, 15, "Perfume amaderado aromático para hombres sofisticados."));
                repository.save(new Producto("Sauvage", "Dior", 130.00, 20, "Fragancia fresca y rústica con notas de bergamota de Calabria."));
                repository.save(new Producto("Acqua Di Gio", "Giorgio Armani", 115.00, 25, "Fragancia mítica, marina, fresca y mediterránea."));
                repository.save(new Producto("One Million", "Paco Rabanne", 95.00, 30, "Perfume intenso, dulce y audaz con notas de cuero y ámbar."));
                repository.save(new Producto("Le Male", "Jean Paul Gaultier", 110.00, 12, "Un aroma magnético y moderno construido sobre contrastes de lavanda y menta."));

                // Catálogo de Perfumes Femeninos
                repository.save(new Producto("La Vie Est Belle", "Lancôme", 125.00, 18, "Fragancia frutal y dulce con un corazón de iris y jazmín."));
                repository.save(new Producto("J adore", "Dior", 150.00, 10, "Un ramo floral sofisticado, glamoroso y ultrafemenino."));
                repository.save(new Producto("Good Girl", "Carolina Herrera", 135.00, 22, "Aroma audaz y sensual en un frasco icónico de tacón aguja."));
                repository.save(new Producto("Black Opium", "Yves Saint Laurent", 145.00, 8, "Perfume adictivo e impactante con notas de café negro y vainilla."));
                repository.save(new Producto("Light Blue", "Dolce & Gabbana", 90.00, 40, "Fragancia fresca, cítrica e ideal para el día a día veraniego."));

                System.out.println("✅ ¡Catálogo premium de Perfulandia cargado exitosamente en la base de datos!");
            }
        };
    }
}