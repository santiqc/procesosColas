package com.procesos.colas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Colas service
 * 
 * Project structure follows Clean Architecture principles:
 * - domain: Core business logic and entities
 * - application: Use cases and business rules
 * - infrastructure: External concerns (database, messaging, etc)
 * - interfaces: Controllers, DTOs and external interfaces
 */
@EnableJms
@EnableScheduling
@SpringBootApplication
public class ColasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColasApplication.class, args);
    }

}
