package com.scottlogic.training.matcher.order;

import com.scottlogic.training.matcher.Direction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to ensure that the validation on incoming Order DTOs is performed correctly
 */
public class OrderDTOTests {

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

    int numberOfViolations(OrderDTO orderDTO) {
        return validator.validate(orderDTO).size();
    }

    void assertViolationIs(OrderDTO orderDTO, String property, String message, Integer value) {
        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(orderDTO);
        assertEquals(1, violations.size());
        ConstraintViolation<OrderDTO> violation = violations.iterator().next();
        assertEquals(message, violation.getMessage());
        assertEquals(property, violation.getPropertyPath().toString());
        assertEquals(value, violation.getInvalidValue());
    }

    @Test
    void acceptsAValidOrder() {
        OrderDTO orderDTO = new OrderDTO("username", 50, 20, Direction.SELL);
        assertEquals(0, numberOfViolations(orderDTO));
    }

    @Test
    void rejectsNegativeQuantity() {
        OrderDTO orderDTO = new OrderDTO("username", 50, -20, Direction.SELL);
        assertViolationIs(
                orderDTO,
                "quantity",
                "must be greater than 0",
                -20
        );
    }

    @Test
    void rejectsZeroQuantity() {
        OrderDTO orderDTO = new OrderDTO("username", 50, 0, Direction.SELL);
        assertViolationIs(
                orderDTO,
                "quantity",
                "must be greater than 0",
                0
        );
    }

    @Test
    void rejectsNegativePrice() {
        OrderDTO orderDTO = new OrderDTO("username", -50, 20, Direction.SELL);
        assertViolationIs(
                orderDTO,
                "price",
                "must be greater than 0",
                -50
        );
    }

    @Test
    void acceptsSellDirection() {
        OrderDTO orderDTO = new OrderDTO("username", 50, 20, Direction.SELL);
        assertEquals(0, numberOfViolations(orderDTO));
    }

    @Test
    void acceptsBuyDirection() {
        OrderDTO orderDTO = new OrderDTO("username", 50, 20, Direction.BUY);
        assertEquals(0, numberOfViolations(orderDTO));
    }

    @Test
    void rejectsNullDirection() {
        OrderDTO orderDTO = new OrderDTO("username", 50, 20, null);
        assertViolationIs(
                orderDTO,
                "direction",
                "must not be null",
                null
        );
    }

}
