package com.martinviscarra.microservices.project.sale_service.dto.sale;

import com.martinviscarra.microservices.project.sale_service.dto.item.SaleItemResponseDto;
import com.martinviscarra.microservices.project.sale_service.utils.SaleStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleResponseDto {

    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private SaleStatus status;
    private List<SaleItemResponseDto> items;
}
