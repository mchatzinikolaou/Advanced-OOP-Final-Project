package com.logistics.controller;

import com.logistics.dto.VehicleRequest;
import com.logistics.dto.VehicleResponse;
import com.logistics.model.Vehicle;
import com.logistics.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody VehicleRequest request) {
        Vehicle vehicle = new Vehicle(
            request.getType(),
            request.getLicensePlate(),
            request.getCapacity(),
            request.getSpeed()
        );
        VehicleResponse savedVehicle = vehicleService.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }

    @PostMapping("/init")
    public ResponseEntity<List<VehicleResponse>> initializeFleet() {
        List<VehicleResponse> vehicles = vehicleService.initializeFleet();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        List<VehicleResponse> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Long id) {
        VehicleResponse vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
}
