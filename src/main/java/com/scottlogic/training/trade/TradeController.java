package com.scottlogic.training.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @GetMapping("/trades")
    public List<Trade> getTrades() {
        return tradeService.getTrades();
    }
}
