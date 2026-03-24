package com.logistics.service;

import com.logistics.dto.VehicleResponse;
import com.logistics.factory.VehicleFactory;
import com.logistics.model.Vehicle;
import com.logistics.model.VehicleType;
import com.logistics.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;
    private final VehicleFactory vehicleFactory;

    @Value("${logistics.fleet.truck-count}")
    private int truckCount;

    @Value("${logistics.fleet.van-count}")
    private int vanCount;

    @Value("${logistics.fleet.drone-count}")
    private int droneCount;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, VehicleFactory vehicleFactory) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleFactory = vehicleFactory;
    }

    @Transactional
    public List<VehicleResponse> initializeFleet() {
        logger.info("Initializing vehicle fleet using Factory Method Pattern");

        List<Vehicle> vehicles = new ArrayList<>();

        // Create all vehicles using Stream API
        List<Vehicle> trucks = IntStream.range(0, truckCount)
            .mapToObj(i -> vehicleFactory.createVehicle(VehicleType.TRUCK))
            .toList();

        List<Vehicle> vans = IntStream.range(0, vanCount)
            .mapToObj(i -> vehicleFactory.createVehicle(VehicleType.VAN))
            .toList();

        List<Vehicle> drones = IntStream.range(0, droneCount)
            .mapToObj(i -> vehicleFactory.createVehicle(VehicleType.DRONE))
            .toList();

        vehicles.addAll(trucks);
        vehicles.addAll(vans);
        vehicles.addAll(drones);

        // Bulk insert all vehicles in one transaction
        List<Vehicle> savedVehicles = vehicleRepository.saveAll(vehicles);

        // Log created vehicles
        savedVehicles.forEach(vehicle ->
            logger.info("Created {}: {} with capacity {}kg",
                vehicle.getType(), vehicle.getLicensePlate(), vehicle.getCapacity())
        );

        logger.info("Fleet initialization complete: {} vehicles created", savedVehicles.size());

        // Convert to DTOs before returning
        return savedVehicles.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public VehicleResponse createVehicle(Vehicle vehicle) {
        logger.info("Creating vehicle with license plate: {}", vehicle.getLicensePlate());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        logger.info("Vehicle created successfully with ID: {}", savedVehicle.getId());
        return convertToResponse(savedVehicle);
    }

    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
        return convertToResponse(vehicle);
    }

    /**
     * Converts Vehicle entity to VehicleResponse DTO
     */
    private VehicleResponse convertToResponse(Vehicle vehicle) {
        return new VehicleResponse(
            vehicle.getId(),
            vehicle.getType(),
            vehicle.getLicensePlate(),
            vehicle.getCapacity(),
            vehicle.getSpeed()
        );
    }
}
