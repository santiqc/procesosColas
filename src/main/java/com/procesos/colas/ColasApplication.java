package com.procesos.colas.interfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Colas service
 * 
 * Project structure follows Clean Architecture principles:
 * - domain: Core business logic and entities
 * - application: Use cases and business rules
 * - infrastructure: External concerns (database, messaging, etc)
 * - interfaces: Controllers, DTOs and external interfaces
 */
@SpringBootApplication
public class ColasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColasApplication.class, args);
    }

}
