package com.scottlogic.training.depthChart;

import java.util.UUID;

public class DepthDataPoint {
    public UUID id;
    public int price;
    public int cumQuantity;

    public DepthDataPoint(int price, int cumQuantity) {
        id = UUID.randomUUID();
        this.price = price;
        this.cumQuantity = cumQuantity;
    }
}
