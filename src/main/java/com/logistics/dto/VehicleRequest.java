package com.logistics.dto;

import com.logistics.model.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VehicleRequest {

    @NotNull(message = "Vehicle type is required")
    private VehicleType type;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private Double capacity;

    @NotNull(message = "Speed is required")
    @Positive(message = "Speed must be positive")
    private Double speed;

    public VehicleRequest() {
    }

    public VehicleRequest(VehicleType type, String licensePlate, Double capacity, Double speed) {
        this.type = type;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.speed = speed;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
}
