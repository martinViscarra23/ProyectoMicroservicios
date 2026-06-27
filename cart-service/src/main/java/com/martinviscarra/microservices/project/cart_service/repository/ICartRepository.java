package com.martinviscarra.microservices.project.cart_service.repository;

import com.martinviscarra.microservices.project.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
}
