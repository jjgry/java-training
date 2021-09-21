package com.scottlogic.training.trade;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Trade implements Cloneable {
    public UUID id;
    public final String buyerUsername;
    public final String sellerUsername;
    public final int price;
    public final int quantity;
    public Date timestamp;

    public Trade(String buyerUsername, String sellerUsername, int price, int quantity) {
        id = UUID.randomUUID();
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.price = price;
        this.quantity = quantity;
        timestamp = new Date();
    }

    public Trade(UUID id, String buyerUsername, String sellerUsername, int price, int quantity, Date timestamp) {
        this.id = id;
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", buyerUsername='" + buyerUsername + '\'' +
                ", sellerUsername='" + sellerUsername + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public Trade clone() {
        try {
            Trade clone = (Trade) super.clone();
            clone.timestamp = (Date) this.timestamp.clone();
            clone.id = UUID.fromString(this.id.toString());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof Trade)) {
            return false;
        }
        Trade otherMember = (Trade) anObject;
        return otherMember.id.equals(this.id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("buyerUsername", buyerUsername);
        map.put("sellerUsername", sellerUsername);
        map.put("price", price);
        map.put("quantity", quantity);
        map.put("timestamp", timestamp);
        return map;
    }
}