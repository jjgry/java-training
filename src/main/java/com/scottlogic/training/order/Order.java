package com.scottlogic.training.order;

import com.scottlogic.training.matcher.Direction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Order implements Cloneable {
    public final String username;
    public final int price;
    public final Direction direction;
    public UUID id;
    public int quantity;
    public Date timestamp;

    public Order(String username, int price, int quantity, Direction direction) {
        id = UUID.randomUUID();
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        timestamp = new Date();
    }

    public Order(UUID id, String username, int price, int quantity, Direction direction, Date timestamp) {
        this.id = id;
        this.username = username;
        this.price = price;
        this.quantity = quantity;
        this.direction = direction;
        this.timestamp = timestamp;
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

    public OrderDTO toOrderDTO() {
        return new OrderDTO(price, quantity, direction);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("price", price);
        map.put("quantity", quantity);
        map.put("direction", direction);
        map.put("timestamp", timestamp);
        return map;
    }
}
