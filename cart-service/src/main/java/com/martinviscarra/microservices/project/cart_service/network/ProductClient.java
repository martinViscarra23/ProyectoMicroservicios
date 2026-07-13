package com.martinviscarra.microservices.project.cart_service.network;

import com.martinviscarra.microservices.project.cart_service.dto.product.ProductResponseDto;
import com.martinviscarra.microservices.project.cart_service.exception.BusinessRuleException;
import com.martinviscarra.microservices.project.cart_service.exception.ServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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

    public List<ProductResponseDto> fallbackGetProductsByIds(List<Long> ids, CallNotPermittedException t) {
        log.error("Circuito abierto (Fail-Fast) al buscar IDs {}. Bloqueando llamada.", ids);
        throw new ServiceUnavailableException("El catálogo de productos está temporalmente fuera de servicio. No podemos procesar su carrito en este momento.");
    }

    public List<ProductResponseDto> fallbackGetProductsByIds(List<Long> ids, FeignException t) {
        log.error("Fallo de red con product-service al buscar IDs {}. Status: {}, Causa: {}", ids, t.status(), t.getMessage());
        throw new ServiceUnavailableException("El catálogo de productos está temporalmente fuera de servicio. No podemos procesar su carrito en este momento.");
    }

    @CircuitBreaker(name = "product-service", fallbackMethod = "fallbackGetProductById")
    public ProductResponseDto getProductById(Long id) {
        return productClient.getProductById(id);
    }

    public ProductResponseDto fallbackGetProductById(Long id, CallNotPermittedException t) {
        log.error("Circuito abierto (Fail-Fast) al buscar ID {}. Bloqueando llamada.", id);
        throw new ServiceUnavailableException("El catálogo de productos está temporalmente fuera de servicio.");
    }

    public ProductResponseDto fallbackGetProductById(Long id, FeignException t) {
        log.error("Fallo de red con product-service al buscar ID {}. Status: {}, Causa: {}", id, t.status(), t.getMessage());
        throw new ServiceUnavailableException("El catálogo de productos está temporalmente fuera de servicio.");
    }
}
