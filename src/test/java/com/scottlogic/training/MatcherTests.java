package com.scottlogic.training;

import com.scottlogic.training.matcher.Matcher;
import com.scottlogic.training.matcher.Direction;
import com.scottlogic.training.matcher.Match;
import com.scottlogic.training.order.Order;
import com.scottlogic.training.trade.Trade;
import com.scottlogic.training.trade.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class MatcherTests {
    @InjectMocks
    private Matcher matcher;

    @Mock
    private TradeService tradeService;

    @BeforeEach
    void initializeMatcher() {
        matcher.state.clear();
        Mockito.doNothing().when(tradeService).addTrade(any());
    }

    @Nested
    class Matching {
        @Nested
        class BuyOrderAndNewSellOrder {
            @BeforeEach
            void initializeOrders() {
                Order order = new Order("username1", 50, 20, Direction.BUY);
                matcher.receiveOrder(order);
            }

            @Test
            void acceptsSamePriceAndQuantity() {
                Order order = new Order("username2", 50, 20, Direction.SELL);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }

            @Test
            void acceptsHigherBuyPrice() {
                Order order = new Order("username2", 40, 20, Direction.SELL);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }

            @Test
            void rejectsHigherSellPrice() {
                Order order = new Order("username2", 60, 20, Direction.SELL);
                Match match = matcher.findMatch(order);
                assertFalse(match.successful);
            }

            @Test
            void acceptsHigherBuyQuantity() {
                Order order = new Order("username2", 50, 10, Direction.SELL);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }

            @Test
            void acceptsHigherSellQuantity() {
                Order order = new Order("username2", 50, 30, Direction.SELL);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }
        }

        @Nested
        class SellOrderAndNewBuyOrder {
            @BeforeEach
            void initializeOrders() {
                Order order = new Order("username1", 50, 20, Direction.SELL);
                matcher.receiveOrder(order);
            }

            @Test
            void acceptsSamePriceAndQuantity() {
                Order order = new Order("username2", 50, 20, Direction.BUY);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }

            @Test
            void acceptsHigherBuyPrice() {
                Order order = new Order("username2", 60, 20, Direction.BUY);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }

            @Test
            void rejectsHigherSellPrice() {
                Order order = new Order("username2", 40, 20, Direction.BUY);
                Match match = matcher.findMatch(order);
                assertFalse(match.successful);
            }

            @Test
            void acceptsHigherBuyQuantity() {
                Order order = new Order("username2", 50, 30, Direction.BUY);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }

            @Test
            void acceptsHigherSellQuantity() {
                Order order = new Order("username2", 50, 10, Direction.BUY);
                Match match = matcher.findMatch(order);
                assertTrue(match.successful);
            }
        }

        @Nested
        class TwoCompetingBuyOrdersAndNewSellOrder {
            @Test
            void choosesHigherValueBuyOrder() {
                Order order1 = new Order("username1", 50, 20, Direction.BUY);
                Order order2 = new Order("username2", 60, 20, Direction.BUY);
                Order order3 = new Order("username3", 50, 20, Direction.SELL);
                matcher.receiveOrder(order1);
                matcher.receiveOrder(order2);
                Match match = matcher.findMatch(order3);
                assertTrue(match.successful);
                assertEquals("username2", match.buyOrder.username);
            }

            @Test
            void choosesNewerBuyOrderWhenPriceEqual() {
                Date date1 = (new Calendar.Builder().setDate(2021, 8, 14).build()).getTime();
                Date date2 = (new Calendar.Builder().setDate(2021, 8, 13).build()).getTime();
                Order order1 = new Order("username1", 50, 20, Direction.BUY);
                Order order2 = new Order("username2", 50, 20, Direction.BUY);
                order1.timestamp = date1;
                order2.timestamp = date2;
                Order order3 = new Order("username3", 50, 20, Direction.SELL);
                matcher.receiveOrder(order1);
                matcher.receiveOrder(order2);
                Match match = matcher.findMatch(order3);
                assertTrue(match.successful);
                assertEquals("username2", match.buyOrder.username);
            }

        }

        @Nested
        class TwoCompetingSellOrdersAndNewBuyOrder {
            @Test
            void choosesCheaperSellOrder() {
                Order order1 = new Order("username1", 50, 20, Direction.SELL);
                Order order2 = new Order("username2", 40, 20, Direction.SELL);
                Order order3 = new Order("username3", 50, 20, Direction.BUY);
                matcher.receiveOrder(order1);
                matcher.receiveOrder(order2);
                Match match = matcher.findMatch(order3);
                assertTrue(match.successful);
                assertEquals("username2", match.sellOrder.username);
            }

            @Test
            void choosesNewerSellOrderWhenPriceEqual() {
                Date date1 = (new Calendar.Builder().setDate(2021, 8, 14).build()).getTime();
                Date date2 = (new Calendar.Builder().setDate(2021, 8, 13).build()).getTime();
                Order order1 = new Order("username1", 50, 20, Direction.SELL);
                Order order2 = new Order("username2", 50, 20, Direction.SELL);
                order1.timestamp = date1;
                order2.timestamp = date2;
                Order order3 = new Order("username3", 50, 20, Direction.BUY);
                matcher.receiveOrder(order1);
                matcher.receiveOrder(order2);
                Match match = matcher.findMatch(order3);
                assertTrue(match.successful);
                assertEquals("username2", match.sellOrder.username);
            }

        }
    }

    @Nested
    class Trading {
        @Test
        void NewOrderOfHigherQuantity() {
            Order order1 = new Order("username1", 50, 10, Direction.BUY);
            Order order2 = new Order("username2", 50, 20, Direction.SELL);
            Match match = new Match(true, order1, order2, 50, Direction.BUY, 20, 10);
            Trade expectedTrade = new Trade("username1", "username2", 50, 10);
            matcher.receiveOrder(order1);

            matcher.makeTrade(match);
            Order updatedOrder2 = order2.clone();
            updatedOrder2.quantity = 10;

            Trade actualTrade = matcher.state.getTrades().get(0);
            assertEquals(List.of(updatedOrder2), matcher.state.getOrders());
            assertEquals(expectedTrade.buyerUsername, actualTrade.buyerUsername);
            assertEquals(expectedTrade.sellerUsername, actualTrade.sellerUsername);
            assertEquals(expectedTrade.price, actualTrade.price);
            assertEquals(expectedTrade.quantity, actualTrade.quantity);
        }

        @Test
        void NewOrderOfEqualQuantity() {
            Order order1 = new Order("username1", 50, 20, Direction.BUY);
            Order order2 = new Order("username2", 50, 20, Direction.SELL);
            Match match = new Match(true, order1, order2, 50, Direction.BUY, 20, 20);
            Trade expectedTrade = new Trade("username1", "username2", 50, 20);
            matcher.receiveOrder(order1);

            matcher.makeTrade(match);

            Trade actualTrade = matcher.state.getTrades().get(0);
            assertEquals(new ArrayList<>(0), matcher.state.getOrders());
            assertEquals(expectedTrade.buyerUsername, actualTrade.buyerUsername);
            assertEquals(expectedTrade.sellerUsername, actualTrade.sellerUsername);
            assertEquals(expectedTrade.price, actualTrade.price);
            assertEquals(expectedTrade.quantity, actualTrade.quantity);
        }

        @Test
        void NewOrderOfLesserQuantity() {
            Order order1 = new Order("username1", 50, 20, Direction.BUY);
            Order order2 = new Order("username2", 50, 10, Direction.SELL);
            Match match = new Match(true, order1, order2, 50, Direction.BUY, 10, 20);
            Trade expectedTrade = new Trade("username1", "username2", 50, 10);
            matcher.receiveOrder(order1);

            matcher.makeTrade(match);
            Order updatedOrder1 = order1.clone();
            updatedOrder1.quantity = 10;

            Trade actualTrade = matcher.state.getTrades().get(0);
            assertEquals(List.of(updatedOrder1), matcher.state.getOrders());
            assertEquals(expectedTrade.buyerUsername, actualTrade.buyerUsername);
            assertEquals(expectedTrade.sellerUsername, actualTrade.sellerUsername);
            assertEquals(expectedTrade.price, actualTrade.price);
            assertEquals(expectedTrade.quantity, actualTrade.quantity);
        }
    }
}
