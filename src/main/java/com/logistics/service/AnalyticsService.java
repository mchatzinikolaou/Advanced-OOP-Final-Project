package com.logistics.service;

import com.logistics.dto.AnalyticsResponse;
import com.logistics.model.Order;
import com.logistics.model.OrderStatus;
import com.logistics.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Analytics Service using Java Stream API (NO SQL Aggregations)
 */
@Service
public class AnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    private final OrderRepository orderRepository;

    @Autowired
    public AnalyticsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public AnalyticsResponse getAnalytics() {
        logger.info("Generating analytics using Java Stream API");

        List<Order> allOrders = orderRepository.findAll();

        // 1. Total revenue from all orders (reduce)
        Double totalRevenue = allOrders.stream()
            .filter(order -> order.getCost() != null)
            .map(Order::getCost)
            .reduce(0.0, Double::sum);

        logger.info("Total revenue calculated: {}€", totalRevenue);

        // 2. Group orders by destination (groupingBy)
        Map<String, Long> ordersByDestination = allOrders.stream()
            .collect(Collectors.groupingBy(
                Order::getDestination,
                Collectors.counting()
            ));

        logger.info("Orders grouped by destination: {} destinations", ordersByDestination.size());

        // 3. Find most expensive order (max)
        AnalyticsResponse.OrderInfo mostExpensiveOrder = allOrders.stream()
            .filter(order -> order.getCost() != null)
            .max(Comparator.comparing(Order::getCost))
            .map(order -> new AnalyticsResponse.OrderInfo(
                order.getId(),
                order.getCustomerName(),
                order.getCost()
            ))
            .orElse(null);

        if (mostExpensiveOrder != null) {
            logger.info("Most expensive order: ID={}, Cost={}€",
                mostExpensiveOrder.getId(), mostExpensiveOrder.getCost());
        }

        // 4. List of IDs for DELAYED orders (filter, map)
        List<Long> delayedOrderIds = allOrders.stream()
            .filter(order -> order.getStatus() == OrderStatus.DELAYED)
            .map(Order::getId)
            .collect(Collectors.toList());

        logger.info("Delayed orders found: {}", delayedOrderIds.size());

        return new AnalyticsResponse(totalRevenue, ordersByDestination, mostExpensiveOrder, delayedOrderIds);
    }
}
