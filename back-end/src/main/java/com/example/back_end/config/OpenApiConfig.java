package com.example.back_end.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI locadoraOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Sistema de Videolocadora")
                        .description(
                                "API REST para administrar o acervo da videolocadora, "
                                        + "incluindo atores, diretores, classes, títulos e itens."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SistemaVideoLocadora")
                                .url("https://github.com/JoaoGamaa/SistemaVideoLocadora"))
                        .license(new License().name("Uso acadêmico")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor local de desenvolvimento")));
    }
}
