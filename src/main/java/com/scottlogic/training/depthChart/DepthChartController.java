package com.scottlogic.training.depthChart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepthChartController {
    @Autowired
    private DepthChartService depthChartService;

    @GetMapping("/depth-chart")
    public DepthChartData getDepthChartData() {
        return depthChartService.getDepthChartData();
    }
}
