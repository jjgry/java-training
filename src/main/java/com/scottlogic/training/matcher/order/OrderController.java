package com.scottlogic.training.matcher.order;

import java.util.concurrent.atomic.AtomicLong;

import com.scottlogic.training.auth.AuthService;
import com.scottlogic.training.matcher.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class OrderController {
    @Autowired
    private Matcher matcher;

    @Autowired
    private AuthService authService;

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/orders")
    public OrdersDTO getOrders() {
        return new OrdersDTO(counter.incrementAndGet(), matcher.state.getOrders());
    }

    @PostMapping("/orders")
    public Order postOrder(
            @RequestHeader(value = "Authorization") String authorisation,
            @RequestBody @Valid OrderDTO orderDTO) {
        String username = authService.getUsername(authorisation);
        Order order = orderDTO.makeOrder(username);
        matcher.receiveOrder(order);
        return order;
    }
}
