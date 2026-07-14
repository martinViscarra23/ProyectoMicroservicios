package com.martinviscarra.microservices.project.sale_service.network;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinviscarra.microservices.project.sale_service.exception.BusinessRuleException;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultDecoder = new Default();
        // Instanciamos ObjectMapper para leer el JSON de respuesta de CartService
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Exception decode(String methodKey, Response response) {
            int status = response.status();

            // Extraemos el mensaje real que viene desde CartService
            String errorMessage = extractErrorMessage(response);

            return switch (status) {
                case 404 -> {
                    // Si logramos extraer el mensaje, lo usamos; si no, usamos uno por defecto
                    String detail = (errorMessage != null) ? errorMessage : "El recurso solicitado no existe en el servicio de carritos.";
                    yield new BusinessRuleException(detail);
                }

                default -> {
                    if (status >= 400 && status < 500) {
                        String detail = (errorMessage != null) ? errorMessage : "Error de solicitud HTTP (" + status + ") al consultar el catálogo.";
                        yield new BusinessRuleException(detail);
                    }

                    // Si es 500+ (Fallo de servidor), delegamos al decoder por defecto para que Resilience4j abra el circuito
                    yield defaultDecoder.decode(methodKey, response);
                }
            };
        }

        /**
         * Método auxiliar para leer el InputStream del body y extraer el campo "detail" o "message"
         */
        private String extractErrorMessage(Response response) {
            if (response.body() == null) {
                return null;
            }

            try (InputStream bodyIs = response.body().asInputStream()) {
                // Mapeamos el JSON entrante a un Map genérico
                Map<String, Object> errorDetails = objectMapper.readValue(bodyIs, new TypeReference<Map<String, Object>>() {});

                // Intentamos obtener el atributo "detail" (Estándar ProblemDetail RFC 7807 de Spring Boot 3)
                if (errorDetails.containsKey("detail") && errorDetails.get("detail") != null) {
                    return errorDetails.get("detail").toString();
                }

                // Fallback: Si no usa ProblemDetail, buscamos el atributo clásico "message"
                if (errorDetails.containsKey("message") && errorDetails.get("message") != null) {
                    return errorDetails.get("message").toString();
                }

            } catch (IOException e) {
                // Si el body no era un JSON válido o hubo un error de lectura, fallamos silenciosamente y devolvemos null para que el switch utilice el mensaje por defecto.
                return null;
            }

            return null;
        }
    }
}