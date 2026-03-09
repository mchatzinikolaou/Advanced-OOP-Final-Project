package com.logistics.strategy;

/**
 * Strategy Pattern: Interface for shipping cost calculation
 */
public interface ShippingStrategy {
    double calculateCost(double weight);
    String getStrategyName();
}
