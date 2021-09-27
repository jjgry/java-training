package com.scottlogic.training.order;

import com.scottlogic.training.matcher.Direction;

import java.util.UUID;

public class AggregatedDataPoint {
    public UUID id;
    public int price;
    public int quantity;
    public Direction direction;

    public AggregatedDataPoint(int price, int quantity, Direction direction) {
        id = UUID.randomUUID();
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
    }
}
