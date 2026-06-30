package com.martinviscarra.microservices.project.product_service.controller;

import com.martinviscarra.microservices.project.product_service.dto.ProductDto;
import com.martinviscarra.microservices.project.product_service.dto.ProductRequestDto;
import com.martinviscarra.microservices.project.product_service.service.IProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final IProductService productService;

    @Value("${server.port}")
    private int serverPort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody ProductRequestDto request) {
        return productService.save(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto edit(@Valid @RequestBody ProductRequestDto request, @PathVariable Long id) {
        return productService.edit(id, request);
    }

    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto activate(@PathVariable Long id) {
        return productService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto deactivate(@PathVariable Long id) {
        return productService.deactivate(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProductsByIds(@RequestParam("ids") List<Long> ids, HttpServletRequest request) {

        List<ProductDto> response = productService.getProductsByIds(ids);

        // request.getLocalPort() consulta el número de puerto real que está usando esta instancia
        System.out.println("----- Datos de productos servidos desde la instancia con puerto " + request.getLocalPort() + " -----");

        return response;
    }

}
