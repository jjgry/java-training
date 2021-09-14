package com.scottlogic.training;

import java.util.Objects;

public class DirectionUtils {
    public static String directionToString(Direction direction) {
        return direction == Direction.BUY ? "BUY" : "SELL";
    }

    public static Direction stringToDirection(String string) {
        return Objects.equals(string, "BUY") ? Direction.BUY : Direction.SELL;
    }
}
