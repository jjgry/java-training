package com.scottlogic.training.matcher.order;

import java.util.List;

public class OrdersDTO {
    public final long id;
    public final List<Order> orders;

    public OrdersDTO(long id, List<Order> orders) {
        this.id = id;
        this.orders = orders;
    }
}