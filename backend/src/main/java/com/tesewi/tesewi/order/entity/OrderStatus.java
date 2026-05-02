package com.tesewi.tesewi.order.entity;

public enum OrderStatus {
    RECEIVED("Recibida"),
    IN_DIAGNOSIS("En Diagnóstico"),
    WAITING_FOR_PARTS("Esperando Repuestos"),
    READY_FOR_PICKUP("Lista para Retirar"),
    DELIVERED("Entregada"),
    CANCELLED("Cancelada");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}