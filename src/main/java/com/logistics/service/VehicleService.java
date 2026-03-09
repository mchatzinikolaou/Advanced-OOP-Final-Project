package com.logistics.service;

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
    public List<Vehicle> initializeFleet() {
        logger.info("Initializing vehicle fleet using Factory Method Pattern");

        List<Vehicle> vehicles = new ArrayList<>();

        // Create trucks
        for (int i = 0; i < truckCount; i++) {
            Vehicle truck = vehicleFactory.createVehicle(VehicleType.TRUCK);
            vehicles.add(vehicleRepository.save(truck));
            logger.info("Created TRUCK: {} with capacity {}kg", truck.getLicensePlate(), truck.getCapacity());
        }

        // Create vans
        for (int i = 0; i < vanCount; i++) {
            Vehicle van = vehicleFactory.createVehicle(VehicleType.VAN);
            vehicles.add(vehicleRepository.save(van));
            logger.info("Created VAN: {} with capacity {}kg", van.getLicensePlate(), van.getCapacity());
        }

        // Create drones
        for (int i = 0; i < droneCount; i++) {
            Vehicle drone = vehicleFactory.createVehicle(VehicleType.DRONE);
            vehicles.add(vehicleRepository.save(drone));
            logger.info("Created DRONE: {} with capacity {}kg", drone.getLicensePlate(), drone.getCapacity());
        }

        logger.info("Fleet initialization complete: {} vehicles created", vehicles.size());
        return vehicles;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }
}
