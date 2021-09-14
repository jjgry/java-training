package com.scottlogic.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingApplication.class, args);

        System.out.println("hello");
        Matcher matcher = new Matcher();
        Order exampleOrder1 = new Order("jjgray", 100, 20, Direction.BUY);
        Order exampleOrder2 = new Order("bpritchard", 100, 10, Direction.SELL);
        matcher.receiveOrder(exampleOrder1);
        System.out.println(matcher.state.getOrders());
        matcher.receiveOrder(exampleOrder2);
        System.out.println(matcher.state.getOrders());
    }

}
