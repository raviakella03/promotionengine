package com.ravi.interview.promotionengine.service;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class PriceCalculator {
    double price = 0;

    public double priceCalculator(Map<String, Double> unitPrice, Map<String, Double> activePromotions, Map<String, Integer> purchasedProducts) {
        int promotionalQuantity;
        boolean applyPromotion = false;
        Set<String> notEligibleForPromotion = new HashSet<>();

        if (null != unitPrice && null != activePromotions && null != purchasedProducts) {
            for (Map.Entry<String, Double> promotionsEntry : activePromotions.entrySet()) {
                if (!promotionsEntry.getKey().contains("+") && purchasedProducts.containsKey(promotionsEntry.getKey().split("")[1])) {
                    //same sku multiple units
                    log.debug("Product From activePromotions: " + promotionsEntry.getKey().split("")[1]);
                    promotionalQuantity = purchasedProducts.get(promotionsEntry.getKey().split("")[1]) / Integer.parseInt(promotionsEntry.getKey().split("")[0]);
                    if (promotionalQuantity >= 1) {
                        if ( ! notEligibleForPromotion.contains(promotionsEntry.getKey().split("")[1])) {
                            price += promotionsEntry.getValue() * promotionalQuantity;
                            purchasedProducts.put(promotionsEntry.getKey().split("")[1],
                                    (purchasedProducts.get(promotionsEntry.getKey().split("")[1]) - (Integer.parseInt(promotionsEntry.getKey().split("")[0]) * promotionalQuantity)));
                            notEligibleForPromotion.add(promotionsEntry.getKey().split("")[1]);
                        }
                    }
                } else {
                    //combination of multiple skus multiple quantities
                    promotionalQuantity = 0;
                    //2A+1B+4C -- promotion
                    //6A+2B+12C -- cart(2 * promotions)
                    log.debug("Product From activePromotions: " + promotionsEntry.getKey());
                    String[] promotionalProductKeys = promotionsEntry.getKey().split("\\+");
                    for (int i = 0; i < promotionalProductKeys.length; i++) {
                        if (! notEligibleForPromotion.contains(promotionalProductKeys[i].split("")[1])) {
                            if (purchasedProducts.containsKey(promotionalProductKeys[i].split("")[1])
                                    && (purchasedProducts.get(promotionalProductKeys[i].split("")[1]) / Integer.parseInt(promotionalProductKeys[i].split("")[0]) >= 1)) {
                                applyPromotion = true;
                                if (i == 0) {
                                    promotionalQuantity = purchasedProducts.get(promotionalProductKeys[i].split("")[1]) / Integer.parseInt(promotionalProductKeys[i].split("")[0]);
                                } else {
                                    if (purchasedProducts.get(promotionalProductKeys[i].split("")[1]) % Integer.parseInt(promotionalProductKeys[i].split("")[0]) == 0
                                            && purchasedProducts.get(promotionalProductKeys[i].split("")[1]) / Integer.parseInt(promotionalProductKeys[i].split("")[0]) < promotionalQuantity) {
                                        promotionalQuantity = purchasedProducts.get(promotionalProductKeys[i].split("")[1]) / Integer.parseInt(promotionalProductKeys[i].split("")[0]);
                                    }
                                }
                            } else {
                                applyPromotion = false;
                                promotionalQuantity = 0;
                                break;
                            }
                        } else {
                            applyPromotion = false;
                            promotionalQuantity = 0;
                            break;
                        }
                    }

                    if (applyPromotion && promotionalQuantity >= 1) {
                        price += promotionsEntry.getValue() * promotionalQuantity;
                        for (String promotionalProductKey : promotionalProductKeys) {
                            int noOfItemsPromotionApplied = Integer.parseInt(promotionalProductKey.split("")[0]) * promotionalQuantity;
                            purchasedProducts.put(promotionalProductKey.split("")[1],
                                    (purchasedProducts.get(promotionalProductKey.split("")[1]) - noOfItemsPromotionApplied));
                        }
                    }
                }
            }

            for (Map.Entry<String, Integer> entry : purchasedProducts.entrySet()) {
                price += unitPrice.get(entry.getKey()) * entry.getValue();
                purchasedProducts.put(entry.getKey(), purchasedProducts.get(entry.getKey()) - entry.getValue());
            }
        } else {
            price = -1;
        }

        return price;
    }
}
