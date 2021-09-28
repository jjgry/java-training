package com.scottlogic.training.tradeHistoryChart;

import com.scottlogic.training.trade.Trade;
import com.scottlogic.training.trade.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.scottlogic.training.depthChart.DepthChartService.getMax;
import static com.scottlogic.training.depthChart.DepthChartService.getMin;

@Service
public class TradeHistoryChartService {
    @Autowired
    private TradeService tradeService;

    public List<TradeHistoryChartDataPoint> getTradeHistoryChart() {

        long INTERVAL = 5000; // 5 seconds
        int NUM_CANDLES = 50;

        // current time rounded down to last 5  interval
        Date now = new Date((new Date().getTime() / INTERVAL) * INTERVAL);
        long startDate = now.getTime() - (INTERVAL * NUM_CANDLES);

        List<Trade> allTrades = tradeService.getTrades();

        int lastClose = 0;
        List<TradeHistoryChartDataPoint> data = new ArrayList<>();
        for (long date = startDate; date <= now.getTime(); date += INTERVAL) {
            List<Trade> trades = new ArrayList<>();

            for (Trade trade : allTrades) {
                if (trade.timestamp.getTime() > date && trade.timestamp.getTime() <= date + INTERVAL) {
                    trades.add(trade.clone());
                }
            }
            List<Integer> prices = trades.stream().map(trade -> trade.price).collect(Collectors.toList());
            int high = getMax(prices);
            int low = getMin(prices);

            trades.sort((a, b) -> (int) (a.timestamp.getTime() - b.timestamp.getTime()));

            int open = lastClose;
            int close;
            if (trades.size() > 0) {
                close = trades.remove(trades.size() - 1).price;
            } else {
                close = lastClose;
            }
            lastClose = close;

            data.add(new TradeHistoryChartDataPoint(date, high, low, open, close));
        }

        return data;
    }
}
