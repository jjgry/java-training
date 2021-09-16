package com.scottlogic.training.matcher.trade;

import java.util.Date;
import java.util.UUID;

public class Trade implements Cloneable {
    public UUID id;
    public String buyerUsername;
    public String sellerUsername;
    public int price;
    public int quantity;
    public Date timestamp;

    public Trade(String buyerUsername, String sellerUsername, int price, int quantity) {
        id = UUID.randomUUID();
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.price = price;
        this.quantity = quantity;
        timestamp = new Date();
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
}
