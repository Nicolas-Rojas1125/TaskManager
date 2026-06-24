package com.taskmanager.auditoria.config;

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
                .title("Auditoria del Sistema")
                .version("1.0.0")
                .description("Registro de acciones y eventos")
                .contact(new Contact()
                    .name("TaskManager Team")
                    .email("taskmanager@example.com")));
    }
}
