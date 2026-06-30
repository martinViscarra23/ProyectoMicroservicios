package com.martinviscarra.microservices.project.sale_service.service;

import com.martinviscarra.microservices.project.sale_service.dto.cart.CartDetailDto;
import com.martinviscarra.microservices.project.sale_service.dto.cart.CartItemDetailDto;
import com.martinviscarra.microservices.project.sale_service.dto.item.SaleItemResponseDto;
import com.martinviscarra.microservices.project.sale_service.dto.sale.SaleResponseDto;
import com.martinviscarra.microservices.project.sale_service.exception.BusinessRuleException;
import com.martinviscarra.microservices.project.sale_service.model.Sale;
import com.martinviscarra.microservices.project.sale_service.model.SaleItem;
import com.martinviscarra.microservices.project.sale_service.repository.ICartClient;
import com.martinviscarra.microservices.project.sale_service.repository.ISaleRepository;
import com.martinviscarra.microservices.project.sale_service.utils.SaleStatus;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService implements ISaleService {

    private final ISaleRepository saleRepository;
    private final ICartClient cartClient;

    //---------------------------------------------------SAVE
    @Transactional
    @Override
    public SaleResponseDto save(Long cartId) {

        CartDetailDto cart = findCartById(cartId);

        if(cart.getItems() == null || cart.getItems().isEmpty()){
            throw new BusinessRuleException("No se puede generar una venta desde un carrito sin productos.");
        }

        Sale saleToSave = Sale.builder()
                .createdAt(LocalDateTime.now())
                .totalPrice(cart.getTotalPrice())
                .status(SaleStatus.COMPLETED)
                .build();
        saleToSave.setItems(cartItemsDtoToSaleItems(cart.getItems(), saleToSave));

        return entitySaleToDto(saleRepository.save(saleToSave));

    }

    private SaleResponseDto entitySaleToDto(Sale sale) {

        return SaleResponseDto.builder()
                .id(sale.getId())
                .createdAt(sale.getCreatedAt())
                .totalPrice(sale.getTotalPrice())
                .status(sale.getStatus())
                .items(entitiesSaleItemsToDto(sale.getItems()))
                .build();

    }

    private List<SaleItemResponseDto> entitiesSaleItemsToDto(List<SaleItem> items) {

        return items.stream()
                .map(item -> SaleItemResponseDto.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .saleId(item.getSale().getId())
                        .productName(item.getProductName())
                        .productBrand(item.getProductBrand())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .build())
                .toList();
    }

    private List<SaleItem> cartItemsDtoToSaleItems(List<CartItemDetailDto> items, Sale sale) {
        return items.stream()
                .map(item -> SaleItem.builder()
                        .sale(sale)
                        .productId(item.getProductId())
                        .productName(item.getNameProduct())
                        .productBrand(item.getBrandProduct())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .build())
                .toList();
    }

    private CartDetailDto findCartById(Long cartId) {
        try {
            return cartClient.getCartById(cartId);
        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException("No existe el carrito con id: " + cartId);
        }

    }

}
