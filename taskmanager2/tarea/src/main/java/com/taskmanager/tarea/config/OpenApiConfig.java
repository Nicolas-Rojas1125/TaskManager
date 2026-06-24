package com.taskmanager.tarea.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Gestion de Tareas")
                .version("1.0.0")
                .description("CRUD y cambio de estado de tareas")
                .contact(new Contact()
                    .name("TaskManager Team")
                    .email("taskmanager@example.com")));
    }
}
