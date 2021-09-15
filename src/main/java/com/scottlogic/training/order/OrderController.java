package com.scottlogic.training.order;

import java.util.concurrent.atomic.AtomicLong;

import com.scottlogic.training.Matcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {
    private final Matcher matcher = new Matcher();
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/orders")
    public OrdersDTO getOrders() {
        return new OrdersDTO(counter.incrementAndGet(), matcher.state.getOrders());
    }

    @PostMapping("/orders")
    public OrdersDTO postOrder(@RequestBody @Valid OrderDTO orderDTO) {
        matcher.receiveOrder(orderDTO.makeOrder());
        return new OrdersDTO(counter.incrementAndGet(), matcher.state.getOrders());
    }
}
