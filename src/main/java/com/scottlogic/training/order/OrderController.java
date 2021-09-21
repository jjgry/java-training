package com.scottlogic.training.order;

import com.scottlogic.training.auth.AuthService;
import com.scottlogic.training.matcher.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private Matcher matcher;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthService authService;

    @GetMapping("/orders")
    public OrdersDTO getOrders(@RequestHeader(value = "Authorization") String authorisation) {
        String username = authService.getUsername(authorisation);
        List<Order> orders = orderService.getOrders(username);
        return new OrdersDTO(orders);
    }

    @PostMapping("/orders")
    public void postOrder(
            @RequestHeader(value = "Authorization") String authorisation,
            @RequestBody @Valid OrderDTO orderDTO) {
        String username = authService.getUsername(authorisation);

        matcher.receiveOrder(orderDTO.makeOrder(username));
    }
}
