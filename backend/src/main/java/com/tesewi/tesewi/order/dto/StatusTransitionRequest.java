package com.tesewi.tesewi.order.dto;

import com.tesewi.tesewi.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class StatusTransitionRequest {

    @NotNull(message = "New status is required")
    private OrderStatus newStatus;

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }
}