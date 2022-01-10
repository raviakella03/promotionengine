package com.ravi.interview.promotionengine.service;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PriceCalculator {
    Map<String, Double> unitPrice = new HashMap<>();
    Map<String, Double> activePromotions = new HashMap<>();
    Map<String, Integer> purchasedProducts = new HashMap<>();
    double price=0;

    public double priceCalculator(Map<String, Double> unitPrice, Map<String, Double> activePromotions, Map<String, Integer> purchasedProducts) {
        this.unitPrice = unitPrice;
        this.activePromotions = activePromotions;
        this.purchasedProducts = purchasedProducts;
        //same sku multiple units
        price += calculatePromotionPriceSameSku();
        for (Map.Entry<String, Integer> entry : this.purchasedProducts.entrySet()) {
            log.debug(entry.getKey() + "," + entry.getValue());
        }
        //combination of multiple skus
        price += calculatePromotionPriceMultiSku();
        return price;
    }

    private double calculatePromotionPriceMultiSku() {
        double promotionPrice = 0;
        boolean applyPromotion = false;
        int promotionalQuantity = 0;
            for (Map.Entry<String, Double> promotionsEntry : this.activePromotions.entrySet()) {
                if (promotionsEntry.getKey().contains("+")) {
                    //2A+1B+4C -- promotion
                    //6A+2B+12C -- cart(2 * promotions)
                    String[] promotionalProductKeys = promotionsEntry.getKey().split("\\+");
                    for (int i = 0; i < promotionalProductKeys.length;i++) {
                        if (this.purchasedProducts.containsKey(promotionalProductKeys[i].split("")[1])
                                && (this.purchasedProducts.get(promotionalProductKeys[i].split("")[1])/Integer.parseInt(promotionalProductKeys[i].split("")[0]) >= 1)) {
                            applyPromotion = true;
                            if (i == 0) {
                                promotionalQuantity = this.purchasedProducts.get(promotionalProductKeys[i].split("")[1])/Integer.parseInt(promotionalProductKeys[i].split("")[0]);
                            } else {
                                if (this.purchasedProducts.get(promotionalProductKeys[i].split("")[1])%Integer.parseInt(promotionalProductKeys[i].split("")[0]) == 0
                                        && this.purchasedProducts.get(promotionalProductKeys[i].split("")[1])/Integer.parseInt(promotionalProductKeys[i].split("")[0]) < promotionalQuantity) {
                                    promotionalQuantity = this.purchasedProducts.get(promotionalProductKeys[i].split("")[1])/Integer.parseInt(promotionalProductKeys[i].split("")[0]);
                                }
                            }
                        } else {
                            applyPromotion = false;
                            promotionalQuantity = 0;
                            break;
                        }
                    }

                    if (applyPromotion && promotionalQuantity >= 1 ) {
                        promotionPrice += promotionsEntry.getValue() * promotionalQuantity;
                        for (int i = 0; i < promotionalProductKeys.length; i++) {
                            int noOfItemsPromotionApplied = Integer.parseInt(promotionalProductKeys[i].split("")[0]) * promotionalQuantity;
                            this.purchasedProducts.put(promotionalProductKeys[i].split("")[1],
                                    (this.purchasedProducts.get(promotionalProductKeys[i].split("")[1]) - noOfItemsPromotionApplied));
                        }
                    }
                }
            }

        for (Map.Entry<String, Integer> entry : this.purchasedProducts.entrySet()) {
            promotionPrice += this.unitPrice.get(entry.getKey()) * entry.getValue();
            this.purchasedProducts.put(entry.getKey(), this.purchasedProducts.get(entry.getKey()) - entry.getValue());
        }
        return  promotionPrice;
    }

    private double calculatePromotionPriceSameSku() {
        double promotionPrice = 0;
        int promotionalQuantity;
        for (Map.Entry<String, Integer> entry : this.purchasedProducts.entrySet()) {
            for (Map.Entry<String, Double> promotionsEntry : this.activePromotions.entrySet()) {
                if (! promotionsEntry.getKey().contains("+") && entry.getKey().equals(promotionsEntry.getKey().split("")[1])) {
                    //same sku multiple units
                    log.debug("Product From purchasedProducts: " + entry.getKey());
                    log.debug("Product From activePromotions: " + promotionsEntry.getKey().split("")[1]);
                    log.debug("quantity From purchasedProducts: " + entry.getKey());
                    log.debug("quantity From activePromotions: " + promotionsEntry.getKey().split("")[1]);
                    promotionalQuantity = entry.getValue()/Integer.parseInt(promotionsEntry.getKey().split("")[0]);
                    if (promotionalQuantity >= 1) {
                        promotionPrice += promotionsEntry.getValue() * promotionalQuantity;
                        this.purchasedProducts.put(entry.getKey(), (entry.getValue()
                                - (Integer.parseInt(promotionsEntry.getKey().split("")[0]) * promotionalQuantity)));
                    }
                }
            }
        }
        return  promotionPrice;
    }
}
