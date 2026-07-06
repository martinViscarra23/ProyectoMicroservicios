package com.martinviscarra.microservices.project.cart_service.network;

import com.martinviscarra.microservices.project.cart_service.dto.product.ProductResponseDto;
import com.martinviscarra.microservices.project.cart_service.exception.BusinessRuleException;
import com.martinviscarra.microservices.project.cart_service.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final IProductClient productClient;

    @CircuitBreaker(name = "product-service", fallbackMethod = "fallbackGetProductsByIds")
    public List<ProductResponseDto> getProductsByIds(List<Long> ids) {
        return productClient.getProductsByIds(ids);
    }

    // El fallback ya no necesita adivinar. Si llegó aquí, fue por un fallo de red/servidor.
    public List<ProductResponseDto> fallbackGetProductsByIds(List<Long> ids, Throwable t) {
        log.error("Fallo de red con product-service (o circuito abierto) al buscar IDs {}. Causa: {}", ids, t.getMessage());
        log.error("Excepción recibida: {}", t.getClass().getName());
        throw new ServiceUnavailableException("El catálogo de productos está temporalmente fuera de servicio. No podemos procesar su carrito en este momento.");
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "fallbackGetProductById")
    public ProductResponseDto getProductById(Long id) {
        return productClient.getProductById(id);
    }

    public ProductResponseDto fallbackGetProductById(Long id, Throwable t) {
        log.error("Fallo de red con product-service (o circuito abierto) al buscar ID {}. Causa: {}", id, t.getMessage());
        log.error("Excepción recibida: {}", t.getClass().getName());
        throw new ServiceUnavailableException("El catálogo de productos está temporalmente fuera de servicio.");
    }
}
