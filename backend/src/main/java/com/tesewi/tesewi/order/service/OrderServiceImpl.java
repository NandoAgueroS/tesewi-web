package com.tesewi.tesewi.order.service;

import com.tesewi.tesewi.order.dto.*;
import com.tesewi.tesewi.order.entity.*;
import com.tesewi.tesewi.order.exception.*;
import com.tesewi.tesewi.order.repository.OrderRepository;
import com.tesewi.tesewi.order.repository.OrderSequenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderSequenceRepository sequenceRepository;

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = Map.of(
            OrderStatus.RECEIVED, Set.of(OrderStatus.IN_DIAGNOSIS, OrderStatus.CANCELLED),
            OrderStatus.IN_DIAGNOSIS, Set.of(OrderStatus.WAITING_FOR_PARTS, OrderStatus.READY_FOR_PICKUP, OrderStatus.CANCELLED),
            OrderStatus.WAITING_FOR_PARTS, Set.of(OrderStatus.READY_FOR_PICKUP, OrderStatus.CANCELLED),
            OrderStatus.READY_FOR_PICKUP, Set.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED)
    );

    public OrderServiceImpl(OrderRepository orderRepository, OrderSequenceRepository sequenceRepository) {
        this.orderRepository = orderRepository;
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setId(generateOrderId());
        order.setClientName(request.getClientName());
        order.setClientPhone(request.getClientPhone());
        order.setClientEmail(request.getClientEmail());
        order.setDeviceType(request.getDeviceType());
        order.setDeviceBrand(request.getDeviceBrand());
        order.setDeviceModel(request.getDeviceModel());
        order.setIssueDescription(request.getIssueDescription());
        order.setStatus(OrderStatus.RECEIVED);

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return OrderResponse.fromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderResponse::fromEntity);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(String id, UpdateOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (request.getClientName() != null) {
            order.setClientName(request.getClientName());
        }
        if (request.getClientPhone() != null) {
            order.setClientPhone(request.getClientPhone());
        }
        if (request.getClientEmail() != null) {
            order.setClientEmail(request.getClientEmail());
        }
        if (request.getDeviceType() != null) {
            order.setDeviceType(request.getDeviceType());
        }
        if (request.getDeviceBrand() != null) {
            order.setDeviceBrand(request.getDeviceBrand());
        }
        if (request.getDeviceModel() != null) {
            order.setDeviceModel(request.getDeviceModel());
        }
        if (request.getIssueDescription() != null) {
            order.setIssueDescription(request.getIssueDescription());
        }
        if (request.getTechnicianNotes() != null) {
            order.setTechnicianNotes(request.getTechnicianNotes());
        }
        if (request.getEstimatedCost() != null) {
            order.setEstimatedCost(request.getEstimatedCost());
        }
        if (request.getFinalCost() != null) {
            order.setFinalCost(request.getFinalCost());
        }

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse transitionStatus(String id, StatusTransitionRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getNewStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(currentStatus, newStatus);
        }

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatus> getValidStatuses() {
        return Arrays.asList(OrderStatus.values());
    }

    private boolean isValidTransition(OrderStatus from, OrderStatus to) {
        Set<OrderStatus> allowed = VALID_TRANSITIONS.get(from);
        return allowed != null && allowed.contains(to);
    }

    private synchronized String generateOrderId() {
        int currentYear = Year.now().getValue();

        OrderSequence seq = sequenceRepository.findById("order_seq")
                .orElseGet(() -> {
                    OrderSequence newSeq = new OrderSequence(currentYear);
                    return sequenceRepository.save(newSeq);
                });

        // Reset sequence if year changed
        if (seq.getOrderYear() != currentYear) {
            seq.setOrderYear(currentYear);
            seq.setSequence(0);
            seq = sequenceRepository.save(seq);
        }

        int nextSeq = seq.incrementAndGet();
        sequenceRepository.save(seq);

        return String.format("ORD-%d-%04d", currentYear, nextSeq);
    }
}