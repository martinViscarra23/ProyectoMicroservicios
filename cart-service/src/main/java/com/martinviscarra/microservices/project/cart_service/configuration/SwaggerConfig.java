package com.martinviscarra.microservices.project.cart_service.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "API Carts",
                description = "Microservicio para la gestion de Carritos de compras",
                version = "1.0.0",
                contact = @Contact(
                        name = "Martin Alejandro Viscarra",
                        url = "https://github.com/martinViscarra23",
                        email = "martinviscarra123@gmail.com"
                )
        ),
        servers = @Server(
                url = "/",
                description = "Servidor API Gateway"
        )
)
public class SwaggerConfig {
}
