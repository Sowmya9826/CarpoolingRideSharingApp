


/*. vehicles/create --post
    vehicles/user/{userId} -- get
    vehicles/update/{id} --put
    vehicles/delete/{id} --delete
*
*  */

package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.VehicleDto;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.respository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Create a new vehicle
     * POST: /vehicles/create
     */
    @PostMapping("/create")
    public ResponseEntity<String> createVehicle(@RequestBody VehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(vehicleDto.getOwner());
        vehicle.setNumber(vehicleDto.getNumber());
        vehicle.setType(vehicleDto.getType());
        vehicle.setName(vehicleDto.getName());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setSeatCount(vehicleDto.getSeatCount());

        vehicleRepository.save(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body("Vehicle created successfully");
    }

    /**
     * Get all vehicles for a specific user
     * GET: /vehicles/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VehicleDto>> getVehiclesByUser(@PathVariable Long userId) {
        List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getOwner() != null && vehicle.getOwner().getId().equals(userId))
                .collect(Collectors.toList());

        if (vehicles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<VehicleDto> vehicleDtos = vehicles.stream().map(vehicle -> {
            VehicleDto vehicleDto = new VehicleDto();
            vehicleDto.setOwner(vehicle.getOwner());
            vehicleDto.setNumber(vehicle.getNumber());
            vehicleDto.setType(vehicle.getType());
            vehicleDto.setName(vehicle.getName());
            vehicleDto.setColor(vehicle.getColor());
            vehicleDto.setSeatCount(vehicle.getSeatCount());
            return vehicleDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(vehicleDtos);
    }

    /**
     * Update an existing vehicle
     * PUT: /vehicles/update/{id}
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateVehicle(@PathVariable Long id, @RequestBody VehicleDto vehicleDto) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        if (vehicleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }

        Vehicle vehicle = vehicleOptional.get();
        vehicle.setOwner(vehicleDto.getOwner());
        vehicle.setNumber(vehicleDto.getNumber());
        vehicle.setType(vehicleDto.getType());
        vehicle.setName(vehicleDto.getName());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setSeatCount(vehicleDto.getSeatCount());

        vehicleRepository.save(vehicle);
        return ResponseEntity.ok("Vehicle updated successfully");
    }

    /**
     * Delete a vehicle by ID
     * DELETE: /vehicles/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        if (vehicleOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }

        vehicleRepository.delete(vehicleOptional.get());
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}
