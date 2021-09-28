package com.scottlogic.training.tradeHistoryChart;

public class TradeHistoryChartDataPoint {
    public long date;
    public int high;
    public int open;
    public int close;
    public int low;

    public TradeHistoryChartDataPoint(long date, int high, int open, int close, int low) {
        this.date = date;
        this.high = high;
        this.open = open;
        this.close = close;
        this.low = low;
    }
}
