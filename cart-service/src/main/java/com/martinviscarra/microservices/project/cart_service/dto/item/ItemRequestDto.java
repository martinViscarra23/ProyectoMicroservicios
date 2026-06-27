package com.martinviscarra.microservices.project.cart_service.dto.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {

    @NotNull(message = "El id de producto es obligatorio")
    private Long productId;

    @NotNull
    @Positive(message = "La cantidad del producto seleccionado es obligatoria y mayor a cero")
    private Integer quantity;

}
