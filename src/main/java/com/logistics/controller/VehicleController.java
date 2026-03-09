package com.logistics.controller;

import com.logistics.model.Vehicle;
import com.logistics.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * POST /vehicles/init - Initialize vehicle fleet using Factory Method
     */
    @PostMapping("/init")
    public ResponseEntity<List<Vehicle>> initializeFleet() {
        List<Vehicle> vehicles = vehicleService.initializeFleet();
        return ResponseEntity.ok(vehicles);
    }

    /**
     * GET /vehicles - Get all vehicles
     */
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    /**
     * GET /vehicles/{id} - Get vehicle by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
}
