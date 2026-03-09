package com.logistics.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory to retrieve the appropriate shipping strategy based on shipping type
 */
@Component
public class ShippingStrategyFactory {

    private final Map<String, ShippingStrategy> strategies;

    @Autowired
    public ShippingStrategyFactory(Map<String, ShippingStrategy> strategies) {
        this.strategies = strategies;
    }

    public ShippingStrategy getStrategy(String shippingType) {
        ShippingStrategy strategy = strategies.get(shippingType.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown shipping type: " + shippingType);
        }
        return strategy;
    }
}
