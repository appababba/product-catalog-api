package com.appababba.product_catalog_api; // Main application package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Enables Spring Boot auto-configuration and component scanning
@SpringBootApplication
public class ProductCatalogApiApplication {

    // Main method - the entry point for the Java application
    public static void main(String[] args) {
        // Launches the Spring Boot application
        SpringApplication.run(ProductCatalogApiApplication.class, args);
    }

}