package com.logistics.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Express Shipping: (Weight * rate per kg) * surcharge multiplier
 */
@Component("EXPRESS")
public class ExpressShippingStrategy implements ShippingStrategy {

    @Value("${logistics.shipping.express.rate-per-kg}")
    private double ratePerKg;

    @Value("${logistics.shipping.express.surcharge-multiplier}")
    private double surchargeMultiplier;

    @Override
    public double calculateCost(double weight) {
        return (weight * ratePerKg) * surchargeMultiplier;
    }

    @Override
    public String getStrategyName() {
        return "EXPRESS";
    }
}
