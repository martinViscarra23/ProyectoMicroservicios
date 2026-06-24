package com.martinviscarra.microservices.project.product_service.service;

import com.martinviscarra.microservices.project.product_service.dto.ProductDto;
import com.martinviscarra.microservices.project.product_service.dto.ProductRequestDto;
import com.martinviscarra.microservices.project.product_service.exception.BusinessRuleException;
import com.martinviscarra.microservices.project.product_service.model.Product;
import com.martinviscarra.microservices.project.product_service.repository.IProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {

    private final IProductRepository productRepository;


    //-----------------------SAVE
    @Transactional
    @Override
    public ProductDto save(ProductRequestDto req) {

        validateName(req.getName());

        Product product = Product.builder()
                .name(req.getName())
                .brand(req.getBrand())
                .price(req.getPrice())
                .active(true)
                .build();

        return entityToDto(productRepository.save(product));
    }

    private void validateName(String productName) {
        boolean exists = productRepository.existsByName(productName);
        if (exists) {
            throw new BusinessRuleException("Ya existe un producto con el nombre: " + productName);
        }
    }

    //-----------------------EDIT
    @Transactional
    @Override
    public ProductDto edit(Long id, ProductRequestDto req) {

        Product productToEdit = findProduct(id);

        validateNameAndIdNot(req.getName(), id);

        productToEdit.setName(req.getName());
        productToEdit.setBrand(req.getBrand());
        productToEdit.setPrice(req.getPrice());

        return entityToDto(productRepository.save(productToEdit));
    }

    private Product findProduct(Long idProduct) {
        return productRepository.findById(idProduct).orElseThrow(() ->
                new EntityNotFoundException("No se encontró el producto con id: " + idProduct)
        );
    }

    private void validateNameAndIdNot(String productName, Long productId) {
        boolean exists = productRepository.existsByNameAndIdNot(productName, productId);
        if (exists) {
            throw new BusinessRuleException("Ya existe un producto con el nombre: " + productName);
        }
    }

    //---------------------------ACTIVATE
    @Transactional
    @Override
    public ProductDto activate(Long id) {

        Product product = findProduct(id);

        if (product.isActive()) {
            throw new BusinessRuleException("Este producto ya tenía el estado 'activo'");
        }

        product.setActive(true);

        return entityToDto(productRepository.save(product));
    }

    //----------------------------DEACTIVATE
    @Transactional
    @Override
    public ProductDto deactivate(Long id) {
        Product product = findProduct(id);

        if (!product.isActive()) {
            throw new BusinessRuleException("Este producto ya tenía el estado 'inactivo'");
        }

        product.setActive(false);

        return entityToDto(productRepository.save(product));
    }


    //--------------------------GetById
    @Transactional(readOnly = true)
    @Override
    public ProductDto getById(Long id) {
        Product product = findProduct(id);
        return entityToDto(product);
    }

    //--------------------------GetProductsByIds
    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByIds(List<Long> ids) {
        return productRepository.findAllById(ids)
                .stream()
                .map(this::entityToDto)
                .toList();
    }


    //--------------------------Converter
    private ProductDto entityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .price(product.getPrice())
                .active(product.isActive())
                .build();
    }


}
