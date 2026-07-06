package com.martinviscarra.microservices.project.cart_service.network;

import com.martinviscarra.microservices.project.cart_service.exception.BusinessRuleException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            int status = response.status();


            return switch (status) {
                case 404 -> new BusinessRuleException("El recurso solicitado no existe en el catálogo de productos.");
                case 400 -> new IllegalArgumentException("La petición hacia el servicio de productos está mal formada o es inválida.");
                default -> {
                    // Si es cualquier otro error de cliente (401, 403, 409, etc.), lo tratamos como regla de negocio
                    if (status >= 400 && status < 500) {
                        yield new BusinessRuleException("Error de solicitud HTTP (" + status + ") al consultar el catálogo.");
                    }
                    // Si es 500+ (Fallo de servidor), delegamos al decoder por defecto para que Resilience4j abra el circuito
                    yield defaultDecoder.decode(methodKey, response);
                }
            };
        }
    }
}
