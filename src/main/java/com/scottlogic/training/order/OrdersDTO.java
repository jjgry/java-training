package com.scottlogic.training.order;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersDTO {
    public final List<OrderDTO> orders;

    public OrdersDTO(List<Order> orders) {
        this.orders = orders.stream().map(Order::toOrderDTO).collect(Collectors.toList());
    }
}