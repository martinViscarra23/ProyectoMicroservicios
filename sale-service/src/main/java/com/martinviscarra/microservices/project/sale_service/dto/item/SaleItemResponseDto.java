package com.martinviscarra.microservices.project.sale_service.dto.item;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleItemResponseDto {

    private Long id;
    private Long productId;
    private Long saleId;
    private String productName;
    private String productBrand;
    private BigDecimal unitPrice;
    private Integer quantity;

}
