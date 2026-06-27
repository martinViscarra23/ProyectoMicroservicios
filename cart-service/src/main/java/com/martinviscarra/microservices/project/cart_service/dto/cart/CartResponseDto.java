package com.martinviscarra.microservices.project.cart_service.dto.cart;

import com.martinviscarra.microservices.project.cart_service.dto.item.ItemResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDto {

    private Long cartId;
    private String status;

    @Builder.Default
    private List<ItemResponseDto> items = new ArrayList<>();

}
