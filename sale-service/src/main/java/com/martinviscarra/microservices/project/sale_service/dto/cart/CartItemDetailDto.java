package com.martinviscarra.microservices.project.sale_service.dto.cart;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDetailDto {

    private Long itemId;
    private Long productId;
    private Integer quantity;
    private String nameProduct;
    private String brandProduct;
    private BigDecimal unitPrice;
    private BigDecimal itemSubTotal;

}
