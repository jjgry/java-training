package com.scottlogic.training.order;

import com.scottlogic.training.direction.Direction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTests {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    int numberOfViolations(Order order) {
        return validator.validate(order).size();
    }

    void assertViolationIs(Order order, String property, String message, Integer value) {
        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        assertEquals(1, violations.size());
        ConstraintViolation<Order> violation = violations.iterator().next();
        assertEquals(message, violation.getMessage());
        assertEquals(property, violation.getPropertyPath().toString());
        assertEquals(value, violation.getInvalidValue());
    }

    @Test
    void acceptsAValidOrder() {
        Order order = new Order("username", 50, 20, Direction.SELL);
        assertEquals(0, numberOfViolations(order));
    }

    @Test
    void rejectsNegativeQuantity() {
        Order order = new Order("username", 50, -20, Direction.SELL);
        assertViolationIs(
                order,
                "quantity",
                "must be greater than 0",
                -20
        );
    }

    @Test
    void rejectsZeroQuantity() {
        Order order = new Order("username", 50, 0, Direction.SELL);
        assertViolationIs(
                order,
                "quantity",
                "must be greater than 0",
                0
        );
    }

    @Test
    void rejectsNegativePrice() {
        Order order = new Order("username", -50, 20, Direction.SELL);
        assertViolationIs(
                order,
                "price",
                "must be greater than 0",
                -50
        );
    }

    @Test
    void acceptsSellDirection() {
        Order order = new Order("username", 50, 20, Direction.SELL);
        assertEquals(0, numberOfViolations(order));
    }

    @Test
    void acceptsBuyDirection() {
        Order order = new Order("username", 50, 20, Direction.BUY);
        assertEquals(0, numberOfViolations(order));
    }

    @Test
    void rejectsNullDirection() {
        Order order = new Order("username", 50, 20, null);
        assertViolationIs(
                order,
                "direction",
                "must not be null",
                null
        );
    }

}
