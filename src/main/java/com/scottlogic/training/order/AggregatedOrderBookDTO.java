package com.scottlogic.training.order;

import java.util.List;
import java.util.stream.Collectors;

public class AggregatedOrderBookDTO {
    public List<AggregatedDataPoint> buyOrders;
    public List<AggregatedDataPoint> sellOrders;

    private final int MAX_LENGTH = 50;

    public AggregatedOrderBookDTO(
            List<AggregatedDataPoint> buyOrders,
            List<AggregatedDataPoint> sellOrders
    ) {
        this.buyOrders = buyOrders.stream().limit(MAX_LENGTH).collect(Collectors.toList());
        this.sellOrders = sellOrders.stream().limit(MAX_LENGTH).collect(Collectors.toList());
    }
}
