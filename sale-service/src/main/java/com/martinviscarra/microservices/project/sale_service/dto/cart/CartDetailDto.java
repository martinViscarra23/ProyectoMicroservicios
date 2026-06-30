package com.martinviscarra.microservices.project.sale_service.dto.cart;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDetailDto {

    private Long id;
    private String status;
    private BigDecimal totalPrice;
    private List<CartItemDetailDto> items;

}
