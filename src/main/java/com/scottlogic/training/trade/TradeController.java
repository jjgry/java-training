package com.scottlogic.training.trade;

import com.scottlogic.training.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @Autowired
    private AuthService authService;

    @GetMapping("/trades")
    public TradesDTO getTrades(@RequestHeader(value = "Authorization") String authorisation) {
        String username = authService.getUsername(authorisation);
        List<Trade> trades = tradeService.getTrades(username);
        return new TradesDTO(trades);
    }
}
