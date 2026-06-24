package com.martinviscarra.microservices.project.product_service.service;

import com.martinviscarra.microservices.project.product_service.dto.ProductDto;
import com.martinviscarra.microservices.project.product_service.dto.ProductRequestDto;

import java.util.List;

public interface IProductService {

    ProductDto save(ProductRequestDto req);

    ProductDto edit(Long id, ProductRequestDto req);

    ProductDto activate(Long id);

    ProductDto deactivate(Long id);

    ProductDto getById(Long id);

    List<ProductDto> getProductsByIds(List<Long> ids);

}
