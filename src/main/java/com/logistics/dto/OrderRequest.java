package com.logistics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Shipping type is required")
    private String shippingType;

    public OrderRequest() {
    }

    public OrderRequest(String customerName, Double weight, String destination, String shippingType) {
        this.customerName = customerName;
        this.weight = weight;
        this.destination = destination;
        this.shippingType = shippingType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }
}
