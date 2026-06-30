package com.martinviscarra.microservices.project.sale_service.repository;

import com.martinviscarra.microservices.project.sale_service.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {
}
