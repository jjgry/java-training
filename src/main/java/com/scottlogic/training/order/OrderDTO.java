package com.scottlogic.training.order;

import com.scottlogic.training.direction.Direction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class OrderDTO {
    @NotNull
    public String username;
    @Positive
    public int price;
    @Positive
    public int quantity;
    @NotNull
    public Direction direction;

    public OrderDTO() {}

    public OrderDTO(String username, int price, int quantity, Direction direction) {
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
    }

    public Order makeOrder() {
        return new Order(username, price, quantity, direction);
    }
}
