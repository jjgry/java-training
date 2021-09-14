package com.scottlogic.training;

import java.util.Date;
import java.util.UUID;

public class Order implements Cloneable {
    public UUID id;
    public String username;
    public int price;
    public int quantity;
    public Direction direction;
    public Date timestamp;

    public Order(String username, int price, int quantity, Direction direction) {
        id = UUID.randomUUID();
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        timestamp = new Date();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", direction=" + direction +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public Order clone() {
        try {
            Order clone = (Order) super.clone();
            clone.timestamp = (Date) this.timestamp.clone();
            clone.id = UUID.fromString(this.id.toString());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof Order)) {
            return false;
        }
        Order otherMember = (Order) anObject;
        return otherMember.id.equals(this.id);
    }
}
