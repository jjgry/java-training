package com.scottlogic.training.matcher;

import com.scottlogic.training.order.Order;
import com.scottlogic.training.trade.Trade;

import java.util.ArrayList;
import java.util.List;

public class State {
    private ArrayList<Order> orders;
    private ArrayList<Trade> trades;

    public State() {
        orders = new ArrayList<>();
        trades = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        ArrayList<Order> ordersClone = new ArrayList<>();
        for (Order order : orders) {
            ordersClone.add(order.clone());
        }
        return ordersClone;
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }

    public void addTrade(Trade trade) {
        trades.add(trade);
    }

    public List<Trade> getTrades() {
        ArrayList<Trade> tradesClone = new ArrayList<>();
        for (Trade trade : trades) {
            tradesClone.add(trade.clone());
        }
        return tradesClone;
    }

    public void clear() {
        orders = new ArrayList<>();
        trades = new ArrayList<>();
    }
}