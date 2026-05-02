package com.tesewi.tesewi.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_sequence")
public class OrderSequence {

    @Id
    @Column(name = "id", length = 20)
    private String id = "order_seq";

    @Column(name = "sequence", nullable = false)
    private int sequence = 0;

    @Column(name = "order_year", nullable = false)
    private int orderYear;

    public OrderSequence() {
    }

    public OrderSequence(int orderYear) {
        this.orderYear = orderYear;
        this.sequence = 0;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getOrderYear() {
        return orderYear;
    }

    public void setOrderYear(int orderYear) {
        this.orderYear = orderYear;
    }

    public int incrementAndGet() {
        return ++sequence;
    }
}