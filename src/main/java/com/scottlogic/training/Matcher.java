package com.scottlogic.training;

import com.scottlogic.training.direction.Direction;
import com.scottlogic.training.match.Match;
import com.scottlogic.training.order.Order;
import com.scottlogic.training.state.State;
import com.scottlogic.training.trade.Trade;

import javax.validation.Valid;
import java.util.List;

public class Matcher {
    public final State state;

    public Matcher() {
        this.state = new State();
    }

    /**
     * @param order The order to validate
     */
    public void receiveOrder(Order order) {
        Match match = findMatch(order);
        if (match.successful) {
            makeTrade(match);
        } else {
            state.addOrder(order);
        }
    }

    /**
     * @param newOrder The order to attempt to match
     * @return A match object containing whether the match was successful
     * and, if so, the metadata attached to it
     */
    public Match findMatch(Order newOrder) {
        List<Order> potentialOrders = state.getOrders(); // need to make deep copy

        potentialOrders.removeIf(existingOrder ->
                existingOrder.direction == newOrder.direction
        );
        potentialOrders.removeIf(existingOrder ->
                newOrder.direction == Direction.BUY
                        ? newOrder.price < existingOrder.price
                        : existingOrder.price < newOrder.price
        );
        potentialOrders.sort((order1, order2) -> {
            if (order1.price > order2.price) {
                return newOrder.direction == Direction.BUY ? 1 : -1;
            } else if (order1.price < order2.price) {
                return newOrder.direction == Direction.BUY ? -1 : 1;
            } else if (order1.timestamp.before(order2.timestamp)) {
                return -1;
            } else if (order1.timestamp.after(order2.timestamp)) {
                return 1;
            } else {
                return 0;
            }
        });

        if (potentialOrders.size() == 0) {
            return Match.getBadMatch();
        }

        Order selectedOrder = potentialOrders.get(0);
        int matchPrice = Math.min(newOrder.price, selectedOrder.price);

        return newOrder.direction == Direction.BUY
                ? new Match(
                true,
                newOrder,
                selectedOrder,
                matchPrice,
                selectedOrder.direction,
                newOrder.quantity,
                selectedOrder.quantity
        )
                : new Match(
                true,
                selectedOrder,
                newOrder,
                matchPrice,
                selectedOrder.direction,
                newOrder.quantity,
                selectedOrder.quantity
        );
    }

    /**
     * @param match The match to perform a trade on
     */
    public void makeTrade(Match match) {
        Trade trade = new Trade(match.buyOrder.username,
                match.sellOrder.username,
                match.price, Math.min(match.newQuantity, match.existingQuantity));

        state.addTrade(trade);

        Order existingOrder = match.existingOrderDirection == Direction.BUY
                ? match.buyOrder
                : match.sellOrder;
        Order newOrder = match.existingOrderDirection == Direction.BUY
                ? match.sellOrder
                : match.buyOrder;
        state.removeOrder(existingOrder);

        if (match.newQuantity > match.existingQuantity) {
            newOrder.quantity = newOrder.quantity - existingOrder.quantity;
            state.addOrder(newOrder);
        } else if (match.newQuantity < match.existingQuantity) {
            existingOrder.quantity = existingOrder.quantity - newOrder.quantity;
            state.addOrder(existingOrder);
        }
    }
}