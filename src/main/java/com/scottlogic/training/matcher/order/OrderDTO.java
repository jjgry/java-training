package com.scottlogic.training.matcher.order;

import com.scottlogic.training.matcher.direction.Direction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderDTO {
    @Positive
    public int price;
    @Positive
    public int quantity;
    @NotNull
    public Direction direction;

    public OrderDTO() {}

    public OrderDTO(int price, int quantity, Direction direction) {
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
    }

    public Order makeOrder(String username) {
        return new Order(username, price, quantity, direction);
    }
}
