package com.martinviscarra.microservices.project.sale_service.service;

import com.martinviscarra.microservices.project.sale_service.dto.sale.SaleResponseDto;

public interface ISaleService {


    public SaleResponseDto save(Long cartId);


}
