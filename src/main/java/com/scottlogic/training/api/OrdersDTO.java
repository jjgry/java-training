package com.scottlogic.training.api;

import com.scottlogic.training.Order;

import java.util.List;

public class OrdersDTO {
    private final long id;
    private final List<Order> orders;

    public OrdersDTO(long id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }

    public long getId() {
        return id;
    }

    public List<Order> getOrders() {
        return orders;
    }
}