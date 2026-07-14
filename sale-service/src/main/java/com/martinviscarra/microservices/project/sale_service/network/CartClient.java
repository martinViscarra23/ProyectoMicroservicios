    package com.martinviscarra.microservices.project.sale_service.network;

    import com.martinviscarra.microservices.project.sale_service.dto.cart.CartDetailDto;
    import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
    import io.github.resilience4j.retry.annotation.Retry;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Component;
    import org.springframework.web.bind.annotation.PathVariable;


    @Component
    @RequiredArgsConstructor
    public class CartClient {

        private final ICartClient cartClient;

        @Retry(name = "cart-service")
        @CircuitBreaker(name = "cart-service")
        public CartDetailDto getCartById(@PathVariable("id") Long idCart) {
            return cartClient.getCartById(idCart);
        }

    }
