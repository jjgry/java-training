package com.scottlogic.training.order;

import com.scottlogic.training.matcher.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private Matcher matcher;

    public List<Order> getOrders(String username) {
        List<Order> orders = matcher.state.getOrders();
        orders.removeIf(order -> !order.username.equals(username));
        return orders;
    }

    public Order addOrder(Order order) {
        matcher.receiveOrder(order);
        return order;
    }
}
