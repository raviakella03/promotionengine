package com.ravi.interview.promotionengine.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorTest {
    Map<String, Double> unitPrice = new HashMap<>();
    Map<String, Double> activePromotions = new HashMap<>();
    Map<String, Integer> purchasedProducts = new HashMap<>();


    @BeforeEach
    void setUnitPrice() {
        unitPrice.put("A", 50.0);
        unitPrice.put("B", 30.0);
        unitPrice.put("C", 20.0);
        unitPrice.put("D", 15.0);
    }

    @BeforeEach
    void setActivePromotions() {
        activePromotions.put("3A", 130.0);
        activePromotions.put("2B", 45.0);
        activePromotions.put("1C+1D", 30.0);
        activePromotions.put("1A+1D", 50.0);
    }

    @AfterEach
    void cleanUp() {
        if (null != unitPrice && null != activePromotions && null != purchasedProducts) {
            unitPrice.clear();
            activePromotions.clear();
            purchasedProducts.clear();
        }
    }

    @Test
    void nullScenario() {
        //null check - returns -1
        purchasedProducts.put("A", 4);//130+50=180
        purchasedProducts.put("B", 5);//90+30=120
        purchasedProducts.put("C", 1);
        purchasedProducts.put("D", 2);//30+15=45
        //Forcing null
        unitPrice = null;
        double expectedOutput = -1;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }

    @Test
    void scenario1() {
        purchasedProducts.put("A", 1);//50
        purchasedProducts.put("B", 1);//30
        purchasedProducts.put("C", 1);//20
        double expectedOutput = 100;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }

    @Test
    void scenario2() {
        purchasedProducts.put("A", 5);//130+100=230
        purchasedProducts.put("B", 5);//90+30=120
        purchasedProducts.put("C", 1);//20
        double expectedOutput = 370;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }

    @Test
    void scenario3() {
        purchasedProducts.put("A", 3);//130
        purchasedProducts.put("B", 5);//90+30=120
        purchasedProducts.put("C", 1);
        purchasedProducts.put("D", 1);//30
        double expectedOutput = 280;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }

    @Test
    void scenario4() {
        purchasedProducts.put("A", 3);//130
        double expectedOutput = 130.0;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }

    @Test
    void scenario5() {
        purchasedProducts.put("A", 3);//130
        purchasedProducts.put("B", 2);//45
        double expectedOutput = 175.0;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }

    @Test
    void scenario6() {
        purchasedProducts.put("A", 3);//130
        purchasedProducts.put("B", 1);//30
        purchasedProducts.put("C", 1);//20
        double expectedOutput = 180.0;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }

    @Test
    void scenario7() {
        //to check if the promotion is mutually exclusive or not
        purchasedProducts.put("A", 4);//130+50=180
        purchasedProducts.put("B", 5);//90+30=120
        purchasedProducts.put("C", 1);
        purchasedProducts.put("D", 2);//30+15=45
        double expectedOutput = 345;
        assertEquals(expectedOutput, new PriceCalculator().priceCalculator(unitPrice, activePromotions, purchasedProducts));
    }
}