package com.scottlogic.training.match;

import com.scottlogic.training.direction.Direction;
import com.scottlogic.training.order.Order;

public class Match {
    public boolean successful;
    public Order buyOrder;
    public Order sellOrder;
    public int price;
    public Direction existingOrderDirection;
    public int newQuantity;
    public int existingQuantity;

    public Match(boolean successful,
                 Order buyOrder,
                 Order sellOrder,
                 int price,
                 Direction existingOrderDirection,
                 int newQuantity,
                 int existingQuantity) {

        this.successful = successful;
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.price = price;
        this.existingOrderDirection = existingOrderDirection;
        this.newQuantity = newQuantity;
        this.existingQuantity = existingQuantity;
    }

    public static Match getBadMatch() {
        return new Match(false, null, null, 0, null, 0, 0);
    }

    @Override
    public String toString() {
        return "Match{" +
                "successful=" + successful +
                ", buyOrder=" + buyOrder +
                ", sellOrder=" + sellOrder +
                ", price=" + price +
                ", existingOrderDirection=" + existingOrderDirection +
                ", newQuantity=" + newQuantity +
                ", existingQuantity=" + existingQuantity +
                '}';
    }
}
