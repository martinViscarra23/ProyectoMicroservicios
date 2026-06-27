package com.martinviscarra.microservices.project.cart_service.service;

import com.martinviscarra.microservices.project.cart_service.dto.cart.CartDetailDto;
import com.martinviscarra.microservices.project.cart_service.dto.cart.CartRequestDto;
import com.martinviscarra.microservices.project.cart_service.dto.cart.CartResponseDto;
import com.martinviscarra.microservices.project.cart_service.dto.item.ItemRequestDto;
import com.martinviscarra.microservices.project.cart_service.dto.item.ItemResponseDto;

public interface ICartService {

    public CartResponseDto save(CartRequestDto request);

    public ItemResponseDto addItem(Long cartId, ItemRequestDto request);

    public void deleteItem(Long cartId, Long productId);

    public CartDetailDto getCartById(Long cartId);
}
