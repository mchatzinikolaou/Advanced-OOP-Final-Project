package com.logistics.factory;

import com.logistics.model.Vehicle;
import com.logistics.model.VehicleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Factory Method Pattern: Creates vehicles based on type
 */
@Component
public class VehicleFactory {

    @Value("${logistics.vehicle.truck.capacity-kg}")
    private double truckCapacity;

    @Value("${logistics.vehicle.truck.speed-kmh}")
    private double truckSpeed;

    @Value("${logistics.vehicle.truck.license-prefix}")
    private String truckPrefix;

    @Value("${logistics.vehicle.van.capacity-kg}")
    private double vanCapacity;

    @Value("${logistics.vehicle.van.speed-kmh}")
    private double vanSpeed;

    @Value("${logistics.vehicle.van.license-prefix}")
    private String vanPrefix;

    @Value("${logistics.vehicle.drone.capacity-kg}")
    private double droneCapacity;

    @Value("${logistics.vehicle.drone.speed-kmh}")
    private double droneSpeed;

    @Value("${logistics.vehicle.drone.license-prefix}")
    private String dronePrefix;

    @Value("${logistics.vehicle.license-separator}")
    private String licenseSeparator;

    @Value("${logistics.vehicle.license-uuid-start}")
    private int uuidStart;

    @Value("${logistics.vehicle.license-uuid-end}")
    private int uuidEnd;

    public Vehicle createVehicle(String vehicleType) {
        VehicleType type;
        try {
            type = VehicleType.valueOf(vehicleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }

        return createVehicle(type);
    }

    public Vehicle createVehicle(VehicleType type) {
        String licensePlate = generateLicensePlate(type);

        return switch (type) {
            case TRUCK -> createTruck(licensePlate);
            case VAN -> createVan(licensePlate);
            case DRONE -> createDrone(licensePlate);
        };
    }

    private Vehicle createTruck(String licensePlate) {
        return new Vehicle(
            VehicleType.TRUCK,
            licensePlate,
            truckCapacity,
            truckSpeed
        );
    }

    private Vehicle createVan(String licensePlate) {
        return new Vehicle(
            VehicleType.VAN,
            licensePlate,
            vanCapacity,
            vanSpeed
        );
    }

    private Vehicle createDrone(String licensePlate) {
        return new Vehicle(
            VehicleType.DRONE,
            licensePlate,
            droneCapacity,
            droneSpeed
        );
    }

    private String generateLicensePlate(VehicleType type) {
        String prefix = switch (type) {
            case TRUCK -> truckPrefix;
            case VAN -> vanPrefix;
            case DRONE -> dronePrefix;
        };
        return prefix + licenseSeparator + UUID.randomUUID().toString().substring(uuidStart, uuidEnd).toUpperCase();
    }
}
