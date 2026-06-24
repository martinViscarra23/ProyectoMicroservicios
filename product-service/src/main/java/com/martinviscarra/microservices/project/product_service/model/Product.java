package com.martinviscarra.microservices.project.product_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 300, nullable = false)
    private String name;

    @Column(name = "brand", length = 300)
    private String brand;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

}
