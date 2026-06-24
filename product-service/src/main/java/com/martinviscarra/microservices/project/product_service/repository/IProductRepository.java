package com.martinviscarra.microservices.project.product_service.repository;

import com.martinviscarra.microservices.project.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    public boolean existsByName(String name);

    public boolean existsByNameAndIdNot(String name, Long id);


}
