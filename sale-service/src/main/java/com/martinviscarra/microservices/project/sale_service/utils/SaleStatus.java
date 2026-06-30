package com.martinviscarra.microservices.project.sale_service.utils;

import lombok.Getter;

@Getter
public enum SaleStatus {

    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String valor;

    SaleStatus(String valor) {
        this.valor = valor;
    }

}
