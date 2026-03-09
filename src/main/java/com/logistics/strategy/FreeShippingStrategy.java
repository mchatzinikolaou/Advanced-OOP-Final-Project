package com.logistics.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Free Shipping: configured cost (applied under certain conditions)
 */
@Component("FREE")
public class FreeShippingStrategy implements ShippingStrategy {

    @Value("${logistics.shipping.free.cost}")
    private double cost;

    @Override
    public double calculateCost(double weight) {
        return cost;
    }

    @Override
    public String getStrategyName() {
        return "FREE";
    }
}
