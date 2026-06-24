package com.martinviscarra.microservices.project.product_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private boolean active;

}
