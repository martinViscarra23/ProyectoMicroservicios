package com.martinviscarra.microservices.project.cart_service.dto.item;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDto {
    private Long productId;
    private Integer quantity;
}
