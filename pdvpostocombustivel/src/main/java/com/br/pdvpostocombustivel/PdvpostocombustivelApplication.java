package com.br.pdvpostocombustivel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// OpenAPI / Swagger
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "PDV Posto Combustível API",
                version = "v1",
                description = "API de exemplo com CRUD de Pessoas (Spring Boot 3 / Java 17).",
                contact = @Contact(name = "guilherme de freitas miranda", email = "user@example.com"),
                license = @License(name = "v1")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Ambiente Local")
        }
)
public class PdvpostocombustivelApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdvpostocombustivelApplication.class, args);

        // Seu bloco de testes antigos pode ficar comentado aqui se quiser.
        // Como estamos usando Swagger, os testes podem ser feitos pela UI:
        // http://localhost:8080/swagger-ui.html
    }
}