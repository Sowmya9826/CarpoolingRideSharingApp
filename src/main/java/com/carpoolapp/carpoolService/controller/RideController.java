package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.RideDto;
import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.respository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideRepository rideRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createRide(@RequestBody RideDto rideDto) {
        Ride ride = new Ride();
        ride.setVehicle(rideDto.getVehicle());
        ride.setPickupLocation(rideDto.getPickupLocation());
        ride.setDestinationLocation(rideDto.getDestinationLocation());
        ride.setStatus(rideDto.getStatus());
        ride.setStartTime(rideDto.getStartTime());
        ride.setEndTime(rideDto.getEndTime());
        ride.setAvailableSeats(rideDto.getAvailableSeats());
        ride.setCreatedDate(rideDto.getCreatedDate());

        rideRepository.save(ride);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ride created successfully");
    }

    /**
     * Get a ride by ID
     * GET: /rides/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RideDto> getRideById(@PathVariable Long id) {
        Optional<Ride> ride = rideRepository.findById(id);
        if (ride.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            RideDto rideDto = convertToDto(ride.get());
            return ResponseEntity.ok(rideDto);
        }
    }

    /**
     * Find all rides
     * GET: /rides/find
     */
    @GetMapping("/find")
    public ResponseEntity<List<RideDto>> findAllRides() {
        List<Ride> rides = rideRepository.findAll();
        List<RideDto> rideDtos = rides.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(rideDtos);
    }

    /**
     * Update an existing ride
     * PUT: /rides/update/{id}
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRide(@PathVariable Long id, @RequestBody RideDto rideDto) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride not found");
        }

        Ride ride = rideOptional.get();
        ride.setVehicle(rideDto.getVehicle());
        ride.setPickupLocation(rideDto.getPickupLocation());
        ride.setDestinationLocation(rideDto.getDestinationLocation());
        ride.setStatus(rideDto.getStatus());
        ride.setStartTime(rideDto.getStartTime());
        ride.setEndTime(rideDto.getEndTime());
        ride.setAvailableSeats(rideDto.getAvailableSeats());
        ride.setCreatedDate(rideDto.getCreatedDate());

        rideRepository.save(ride);
        return ResponseEntity.ok("Ride updated successfully");
    }

    /**
     * Delete a ride by ID
     * DELETE: /rides/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRide(@PathVariable Long id) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride not found");
        }

        rideRepository.delete(rideOptional.get());
        return ResponseEntity.ok("Ride deleted successfully");
    }

    /**
     * Convert Ride entity to RideDto
     */
    private RideDto convertToDto(Ride ride) {
        RideDto rideDto = new RideDto();
        rideDto.setVehicle(ride.getVehicle());
        rideDto.setPickupLocation(ride.getPickupLocation());
        rideDto.setDestinationLocation(ride.getDestinationLocation());
        rideDto.setStatus(ride.getStatus());
        rideDto.setStartTime(ride.getStartTime());
        rideDto.setEndTime(ride.getEndTime());
        rideDto.setAvailableSeats(ride.getAvailableSeats());
        rideDto.setCreatedDate(ride.getCreatedDate());
        return rideDto;
    }
}
