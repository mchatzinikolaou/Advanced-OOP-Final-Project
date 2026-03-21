package com.logistics.dto;

import com.logistics.model.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
