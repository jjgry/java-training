package com.scottlogic.training.depthChart;

import com.google.common.collect.Lists;
import com.scottlogic.training.matcher.Direction;
import com.scottlogic.training.order.AggregatedDataPoint;
import com.scottlogic.training.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class DepthChartService {
    @Autowired
    private OrderService orderService;

    private int getMidMarketPrice(
            Collection<AggregatedDataPoint> buyOrders,
            Collection<AggregatedDataPoint> sellOrders
    ) {
        int maxBuyPrice = 0;
        int minSellPrice = 0;
        Optional<AggregatedDataPoint> maxBuy = buyOrders.stream().max(Comparator.comparingInt(a -> a.price));
        Optional<AggregatedDataPoint> minSell = sellOrders.stream().min(Comparator.comparingInt(a -> a.price));
        if (maxBuy.isPresent()) {
            maxBuyPrice = maxBuy.get().price;
        }
        if (minSell.isPresent()) {
            minSellPrice = minSell.get().price;
        }
        return (maxBuyPrice + minSellPrice) / 2;
    }

    private int getMin(Collection<Integer> collection) {
        Optional<Integer> min = collection.stream().min(Comparator.comparingInt(a -> a));
        return min.orElse(0);
    }

    private int getMax(Collection<Integer> collection) {
        Optional<Integer> max = collection.stream().max(Comparator.comparingInt(a -> a));
        return max.orElse(0);
    }

    private List<DepthDataPoint> getDepthDataPoints(
            List<AggregatedDataPoint> aggregatedDataPoints,
            Direction direction,
            int binSize,
            int binStart
    ) {
        Map<Integer, Integer> binned = aggregatedDataPoints.stream()
                .map(datapoint -> new BinnedQuantity(
                        Math.floorDiv(datapoint.price - binStart, binSize),
                        datapoint.quantity))
                .collect(Collectors.groupingBy(
                        binnedQuantity -> binnedQuantity.binIndex,
                        Collectors.summingInt(binnedQuantity -> binnedQuantity.quantity)));

        List<BinnedQuantity> sortedBins = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : binned.entrySet()) {
            sortedBins.add(new BinnedQuantity(entry.getKey(), entry.getValue()));
        }
        sortedBins.sort(Comparator.comparingInt(binnedQuantity -> binnedQuantity.binIndex));

        if (direction == Direction.BUY) {
            sortedBins = Lists.reverse(sortedBins);
        }

        List<BinnedQuantity> cumSums = new ArrayList<>();
        for (int i = 0; i < sortedBins.size(); i++) {
            BinnedQuantity binnedQuantity = sortedBins.get(i);
            if (cumSums.size() == 0) {
                cumSums.add(binnedQuantity);
                continue;
            }
            int cumSum = binnedQuantity.quantity + cumSums.get(i - 1).quantity;
            cumSums.add(new BinnedQuantity(binnedQuantity.binIndex, cumSum));

        }

        if (direction == Direction.BUY) {
            cumSums = Lists.reverse(cumSums);
        }

        return cumSums.stream()
                .map(binnedQuantity -> new DepthDataPoint(
                        binnedQuantity.binIndex * binSize + binStart,
                        binnedQuantity.quantity))
                .collect(Collectors.toList());
    }

    class BinnedQuantity {
        int binIndex;
        int quantity;

        public BinnedQuantity(int binIndex, int quantity) {
            this.binIndex = binIndex;
            this.quantity = quantity;
        }
    }

    public DepthChartData getDepthChartData() {
        List<AggregatedDataPoint> buyOrders = orderService.getAggregatedOrders(Direction.BUY);
        List<AggregatedDataPoint> sellOrders = orderService.getAggregatedOrders(Direction.SELL);
        Collection<Integer> allPrices = Stream.of(buyOrders, sellOrders)
                .flatMap(Collection::stream)
                .map(order -> order.price)
                .collect(Collectors.toSet());

        int binStart = getMin(allPrices);
        int binEnd = getMax(allPrices);
        int numBins = 101;
        int binSize = Math.floorDiv(binEnd - binStart, numBins);

        List<DepthDataPoint> buyData = getDepthDataPoints(buyOrders, Direction.BUY, binSize, binStart);
        List<DepthDataPoint> sellData = getDepthDataPoints(sellOrders, Direction.SELL, binSize, binStart);

        return new DepthChartData(buyData, sellData, getMidMarketPrice(buyOrders, sellOrders));
    }
}
