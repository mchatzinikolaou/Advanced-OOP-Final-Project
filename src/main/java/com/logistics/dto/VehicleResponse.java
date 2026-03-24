package com.logistics.dto;

import com.logistics.model.VehicleType;

public class VehicleResponse {
    private Long id;
    private VehicleType type;
    private String licensePlate;
    private Double capacity;
    private Double speed;

    public VehicleResponse() {
    }

    public VehicleResponse(Long id, VehicleType type, String licensePlate, Double capacity, Double speed) {
        this.id = id;
        this.type = type;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.speed = speed;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
