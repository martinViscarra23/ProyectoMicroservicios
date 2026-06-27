package com.martinviscarra.microservices.project.cart_service.dto.cart;

import com.martinviscarra.microservices.project.cart_service.dto.item.ItemRequestDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequestDto {

    @NotEmpty(message = "El carrito debe contener al menos un producto")
    @Builder.Default
    private List<ItemRequestDto> items = new ArrayList<>();

}
