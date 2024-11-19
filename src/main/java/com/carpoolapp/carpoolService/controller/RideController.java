package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.RideDto;
import com.carpoolapp.carpoolService.models.*;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import com.carpoolapp.carpoolService.models.enums.RideStatus;
import com.carpoolapp.carpoolService.models.enums.RideType;
import com.carpoolapp.carpoolService.respository.LocationRepository;
import com.carpoolapp.carpoolService.respository.RideRepository;
import com.carpoolapp.carpoolService.respository.VehicleRepository;
import com.carpoolapp.carpoolService.service.LocationService;
import com.carpoolapp.carpoolService.service.RideParticipantService;
import com.carpoolapp.carpoolService.service.RideService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private RideService rideService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RideParticipantService rideParticipantService;


    @GetMapping("/")
    public String showRidesPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }



        return "rides/show_rides";
    }


    @GetMapping("/create")
    public String showCreateRidePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        // Fetch the user's vehicles
        List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getOwner() != null && vehicle.getOwner().getId().equals(userId))
                .collect(Collectors.toList());

        model.addAttribute("vehicles", vehicles);

        return "rides/create_ride_form";
    }


    @PostMapping("/create")
    public String createRide(RideDto rideDto, HttpSession session, RedirectAttributes redirectAttributes) {
        Long vehicleId = rideDto.getVehicleId();
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Vehicle not found");
            return "redirect:/rides/create";
        }

        // Create start and end locations
        Location startLocation = locationService.createLocation(rideDto.getStartLatitude(), rideDto.getStartLongitude(), rideDto.getStartAddress());
        Location endLocation = locationService.createLocation(rideDto.getEndLatitude(), rideDto.getEndLongitude(), rideDto.getEndAddress());

        // Create the ride
        Ride createdRide = rideService.createRide(rideDto, vehicleOptional.get(), startLocation, endLocation);

        // create a ride participant for the driver
        User driver = vehicleOptional.get().getOwner();
        rideParticipantService.createRideParticipantAsDriver(createdRide, driver);


        redirectAttributes.addFlashAttribute("message", "Ride created successfully!");

        return "redirect:/rides/";
    }

//    /**
//     * Get a ride by ID
//     * GET: /rides/{id}
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<RideDto> getRideById(@PathVariable Long id) {
//        Optional<Ride> ride = rideRepository.findById(id);
//        if (ride.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } else {
//            RideDto rideDto = convertToDto(ride.get());
//            return ResponseEntity.ok(rideDto);
//        }
//    }
//
//    /**
//     * Find all rides
//     * GET: /rides/find
//     */
//    @GetMapping("/find")
//    public ResponseEntity<List<RideDto>> findAllRides() {
//        List<Ride> rides = rideRepository.findAll();
//        List<RideDto> rideDtos = rides.stream().map(this::convertToDto).collect(Collectors.toList());
//        return ResponseEntity.ok(rideDtos);
//    }
//
//    /**
//     * Update an existing ride
//     * PUT: /rides/update/{id}
//     */
//    @PutMapping("/update/{id}")
//    public ResponseEntity<String> updateRide(@PathVariable Long id, @RequestBody RideDto rideDto) {
//        Optional<Ride> rideOptional = rideRepository.findById(id);
//        if (rideOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride not found");
//        }
//
//        Ride ride = rideOptional.get();
//        ride.setVehicle(rideDto.getVehicle());
//        ride.setPickupLocation(rideDto.getPickupLocation());
//        ride.setDestinationLocation(rideDto.getDestinationLocation());
//        ride.setStatus(rideDto.getStatus());
//        ride.setStartTime(rideDto.getStartTime());
//        ride.setEndTime(rideDto.getEndTime());
//        ride.setAvailableSeats(rideDto.getAvailableSeats());
//        ride.setCreatedDate(rideDto.getCreatedDate());
//
//        rideRepository.save(ride);
//        return ResponseEntity.ok("Ride updated successfully");
//    }
//
//    /**
//     * Delete a ride by ID
//     * DELETE: /rides/delete/{id}
//     */
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteRide(@PathVariable Long id) {
//        Optional<Ride> rideOptional = rideRepository.findById(id);
//        if (rideOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride not found");
//        }
//
//        rideRepository.delete(rideOptional.get());
//        return ResponseEntity.ok("Ride deleted successfully");
//    }
//
//    /**
//     * Convert Ride entity to RideDto
//     */
//    private RideDto convertToDto(Ride ride) {
//        RideDto rideDto = new RideDto();
//        rideDto.setVehicle(ride.getVehicle());
//        rideDto.setPickupLocation(ride.getPickupLocation());
//        rideDto.setDestinationLocation(ride.getDestinationLocation());
//        rideDto.setStatus(ride.getStatus());
//        rideDto.setStartTime(ride.getStartTime());
//        rideDto.setEndTime(ride.getEndTime());
//        rideDto.setAvailableSeats(ride.getAvailableSeats());
//        rideDto.setCreatedDate(ride.getCreatedDate());
//        return rideDto;
//    }
}
