package com.ravi.interview.promotionengine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

    @Test
    void scenario1() {
        Map<String, Integer> purchasedProducts = new HashMap<>();
        purchasedProducts.put("A", 1);
        purchasedProducts.put("B", 1);
        purchasedProducts.put("C", 1);
        double expectedOutput = 100;
    }

    @Test
    void scenario2() {
        Map<String, Integer> purchasedProducts = new HashMap<>();
        purchasedProducts.put("A", 5);
        purchasedProducts.put("B", 5);
        purchasedProducts.put("C", 1);
        double expectedOutput = 370;
    }

    @Test
    void scenario3() {
        Map<String, Integer> purchasedProducts = new HashMap<>();
        purchasedProducts.put("A", 3);
        purchasedProducts.put("B", 5);
        purchasedProducts.put("C", 1);
        purchasedProducts.put("D", 1);
        double expectedOutput = 280;
    }
}