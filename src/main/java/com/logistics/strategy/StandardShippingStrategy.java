package com.logistics.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Standard Shipping: Weight * rate per kg
 */
@Component("STANDARD")
public class StandardShippingStrategy implements ShippingStrategy {

    @Value("${logistics.shipping.standard.rate-per-kg}")
    private double ratePerKg;

    @Override
    public double calculateCost(double weight) {
        return weight * ratePerKg;
    }

    @Override
    public String getStrategyName() {
        return "STANDARD";
    }
}
