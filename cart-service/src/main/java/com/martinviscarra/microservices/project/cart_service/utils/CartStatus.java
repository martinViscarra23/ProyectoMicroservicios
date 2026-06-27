package com.martinviscarra.microservices.project.cart_service.utils;

public enum CartStatus {

    OPEN("open"),
    CLOSED("closed");

    private final String valor;

    CartStatus(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

}
