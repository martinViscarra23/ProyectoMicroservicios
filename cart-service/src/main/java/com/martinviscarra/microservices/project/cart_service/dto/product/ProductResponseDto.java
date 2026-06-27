package com.martinviscarra.microservices.project.cart_service.dto.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private boolean active;

}
