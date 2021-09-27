package com.scottlogic.training.order;

import java.util.List;

public class OrdersDTO {
    public final List<Order> orders;

    public OrdersDTO(List<Order> orders) {
        this.orders = orders;
    }
}