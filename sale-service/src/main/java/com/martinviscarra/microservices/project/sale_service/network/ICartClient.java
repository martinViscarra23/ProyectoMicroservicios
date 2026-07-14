package com.martinviscarra.microservices.project.sale_service.network;

import com.martinviscarra.microservices.project.sale_service.dto.cart.CartDetailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service", configuration = FeignClientConfig.class)
public interface ICartClient {

    @GetMapping("/api/carts/{id}")
    public CartDetailDto getCartById(@PathVariable("id") Long idCart);

}
