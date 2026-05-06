package com.tesewi.tesewi.order.service;

import com.tesewi.tesewi.order.dto.*;
import com.tesewi.tesewi.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(String id);

    OrderResponse getOrderByNumber(String orderNumber);

    Page<OrderResponse> getAllOrders(Pageable pageable);

    OrderResponse updateOrder(String id, UpdateOrderRequest request);

    OrderResponse transitionStatus(String id, StatusTransitionRequest request);

    List<OrderStatus> getValidStatuses();
}