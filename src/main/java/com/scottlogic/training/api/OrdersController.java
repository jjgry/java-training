package com.scottlogic.training.api;

import java.util.concurrent.atomic.AtomicLong;

import com.scottlogic.training.Matcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {
    private final Matcher matcher = new Matcher();
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/orders")
    public OrdersDTO getOrders() {
        return new OrdersDTO(counter.incrementAndGet(), matcher.state.getOrders());
    }

    @PostMapping("/orders")
    public OrdersDTO postOrder(@RequestBody OrderRequest orderRequest) {
        matcher.receiveOrder(orderRequest.makeOrder());
        return new OrdersDTO(counter.incrementAndGet(), matcher.state.getOrders());
    }
}
