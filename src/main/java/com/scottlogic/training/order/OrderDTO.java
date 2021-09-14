package com.scottlogic.training.order;

import com.scottlogic.training.direction.Direction;

import static com.scottlogic.training.direction.DirectionUtils.stringToDirection;

public class OrderDTO {
    public String username;
    public int price;
    public int quantity;
    public Direction direction;

    public OrderDTO(String username, int price, int quantity, String direction) {
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.direction = stringToDirection(direction);
    }

    public String getUsername() {
        return username;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Direction Direction() {
        return direction;
    }

    public Order makeOrder() {
        return new Order(username, price, quantity, direction);
    }
}
