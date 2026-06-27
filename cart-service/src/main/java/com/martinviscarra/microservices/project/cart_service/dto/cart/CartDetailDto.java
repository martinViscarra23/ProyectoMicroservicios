package com.martinviscarra.microservices.project.cart_service.dto.cart;

import com.martinviscarra.microservices.project.cart_service.dto.item.ItemDetailDto;
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
    private List<ItemDetailDto> items;

}
