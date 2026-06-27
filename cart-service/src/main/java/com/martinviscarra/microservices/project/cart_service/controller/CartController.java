package com.martinviscarra.microservices.project.cart_service.controller;

import com.martinviscarra.microservices.project.cart_service.dto.cart.CartDetailDto;
import com.martinviscarra.microservices.project.cart_service.dto.cart.CartRequestDto;
import com.martinviscarra.microservices.project.cart_service.dto.cart.CartResponseDto;
import com.martinviscarra.microservices.project.cart_service.dto.item.ItemRequestDto;
import com.martinviscarra.microservices.project.cart_service.dto.item.ItemResponseDto;
import com.martinviscarra.microservices.project.cart_service.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponseDto create(@RequestBody @Valid CartRequestDto request){
        return cartService.save(request);
    }

    @PostMapping("/{id}/add")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto addItem(@PathVariable("id") Long cartId, @RequestBody @Valid ItemRequestDto request){
        return cartService.addItem(cartId, request);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.deleteItem(cartId, productId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CartDetailDto getCartById(@PathVariable("id") Long cartId){
        return cartService.getCartById(cartId);
    }

}
