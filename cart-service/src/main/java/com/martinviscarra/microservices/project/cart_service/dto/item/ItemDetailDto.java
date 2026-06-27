package com.martinviscarra.microservices.project.cart_service.dto.item;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetailDto {

    private Long itemId;
    private Long productId;
    private Integer quantity;
    private String nameProduct;
    private String brandProduct;
    private BigDecimal unitPrice;
    private BigDecimal itemSubTotal;

}
