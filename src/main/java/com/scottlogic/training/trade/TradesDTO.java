package com.scottlogic.training.trade;

import java.util.List;

public class TradesDTO {
    public final List<Trade> trades;

    public TradesDTO(List<Trade> trades) {
        this.trades = trades;
    }
}