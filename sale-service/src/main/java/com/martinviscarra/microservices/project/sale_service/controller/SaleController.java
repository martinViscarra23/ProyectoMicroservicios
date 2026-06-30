package com.martinviscarra.microservices.project.sale_service.controller;

import com.martinviscarra.microservices.project.sale_service.dto.sale.SaleResponseDto;
import com.martinviscarra.microservices.project.sale_service.service.ISaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final ISaleService saleService;


    //--------------------------------POST
    @PostMapping("/checkout/{cartId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponseDto create(@PathVariable("cartId") Long cartId){
        return saleService.save(cartId);
    }

}
