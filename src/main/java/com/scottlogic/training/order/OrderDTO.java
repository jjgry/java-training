package com.scottlogic.training.order;

import com.scottlogic.training.direction.Direction;

public class OrderDTO {
    public final String username;
    public final int price;
    public final int quantity;
    public final Direction direction;

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
