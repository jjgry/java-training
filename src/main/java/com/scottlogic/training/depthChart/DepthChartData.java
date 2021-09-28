package com.scottlogic.training.depthChart;

import java.util.List;

public class DepthChartData {
    public List<DepthDataPoint> buyData;
    public List<DepthDataPoint> sellData;
    public int midMarketPrice;

    public DepthChartData(
            List<DepthDataPoint> buyData,
            List<DepthDataPoint> sellData,
            int midMarketPrice
    ) {
        this.buyData = buyData;
        this.sellData = sellData;
        this.midMarketPrice = midMarketPrice;
    }
}
