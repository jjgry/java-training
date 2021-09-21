package com.scottlogic.training.matcher.order;

import java.util.concurrent.atomic.AtomicLong;

import com.scottlogic.training.matcher.Matcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.scottlogic.training.auth.AuthController.KEY;

@RestController
public class OrderController {
    @Autowired
    private Matcher matcher;
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/orders")
    public OrdersDTO getOrders() {
        return new OrdersDTO(counter.incrementAndGet(), matcher.state.getOrders());
    }

    @PostMapping("/orders")
    public Order postOrder(@RequestHeader(value = "Authorization") String authorisation, @RequestBody @Valid OrderDTO orderDTO) {
        String tokenString = authorisation.replace("Bearer ", "");
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(tokenString)
                .getBody();

        String username = claims.getSubject();
        Order order = orderDTO.makeOrder(username);
        matcher.receiveOrder(order);
        return order;
    }
}
