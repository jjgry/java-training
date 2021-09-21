package com.scottlogic.training.matcher.match;

import com.scottlogic.training.matcher.direction.Direction;
import com.scottlogic.training.matcher.order.Order;

public class Match {
    public final boolean successful;
    public final Order buyOrder;
    public final Order sellOrder;
    public final int price;
    public final Direction existingOrderDirection;
    public final int newQuantity;
    public final int existingQuantity;

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
