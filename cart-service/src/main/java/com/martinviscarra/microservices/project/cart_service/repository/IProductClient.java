package com.martinviscarra.microservices.project.cart_service.repository;

import com.martinviscarra.microservices.project.cart_service.dto.product.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service")
public interface IProductClient {

    @GetMapping("/api/products/batch")
    public List<ProductResponseDto> getProductsByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/api/products/{id}")
    public ProductResponseDto getProductById(@PathVariable("id") Long id);

}
