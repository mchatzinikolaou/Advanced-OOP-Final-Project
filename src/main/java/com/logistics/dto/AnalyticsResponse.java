package com.logistics.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsResponse {

    private Double totalRevenue;
    private Map<String, Long> ordersByDestination;
    private OrderInfo mostExpensiveOrder;
    private List<Long> delayedOrderIds;

    public AnalyticsResponse() {
    }

    public AnalyticsResponse(Double totalRevenue, Map<String, Long> ordersByDestination,
                           OrderInfo mostExpensiveOrder, List<Long> delayedOrderIds) {
        this.totalRevenue = totalRevenue;
        this.ordersByDestination = ordersByDestination;
        this.mostExpensiveOrder = mostExpensiveOrder;
        this.delayedOrderIds = delayedOrderIds;
    }

    // Getters and Setters
    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Map<String, Long> getOrdersByDestination() {
        return ordersByDestination;
    }

    public void setOrdersByDestination(Map<String, Long> ordersByDestination) {
        this.ordersByDestination = ordersByDestination;
    }

    public OrderInfo getMostExpensiveOrder() {
        return mostExpensiveOrder;
    }

    public void setMostExpensiveOrder(OrderInfo mostExpensiveOrder) {
        this.mostExpensiveOrder = mostExpensiveOrder;
    }

    public List<Long> getDelayedOrderIds() {
        return delayedOrderIds;
    }

    public void setDelayedOrderIds(List<Long> delayedOrderIds) {
        this.delayedOrderIds = delayedOrderIds;
    }

    public static class OrderInfo {
        private Long id;
        private String customerName;
        private Double cost;

        public OrderInfo() {
        }

        public OrderInfo(Long id, String customerName, Double cost) {
            this.id = id;
            this.customerName = customerName;
            this.cost = cost;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public Double getCost() {
            return cost;
        }

        public void setCost(Double cost) {
            this.cost = cost;
        }
    }
}
