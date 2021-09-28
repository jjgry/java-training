package com.scottlogic.training.tradeHistoryChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeHistoryChartController {
    @Autowired
    private TradeHistoryChartService tradeHistoryChartService;

    @GetMapping("/trade-history-chart")
    public List<TradeHistoryChartDataPoint> getTradeHistoryChart() {
        return tradeHistoryChartService.getTradeHistoryChart();
    }
}
