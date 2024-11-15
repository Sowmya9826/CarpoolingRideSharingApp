package com.carpoolapp.carpoolService.controller;
import com.carpoolapp.carpoolService.dto.FareDto;
import com.carpoolapp.carpoolService.models.Fare;
import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.respository.FareRepository;
import com.carpoolapp.carpoolService.respository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

    @RestController
    @RequestMapping("/fares")
    public class FareController {

        @Autowired
        private FareRepository fareRepository;
        @Autowired
        private RideRepository rideRepository;

        /**
         * Create a new fare
         * POST: /fares/create
         */
//        @PostMapping("/create")
//        public ResponseEntity<String> createFare(@RequestBody FareDto fareDto) {
//            Fare fare = new Fare();
//            fare.setRide(fareDto.getRide());
//            fare.setAmount(fareDto.getAmount());
//            fare.setStatus(fareDto.getStatus());
//
//            fareRepository.save(fare);
//            return ResponseEntity.status(HttpStatus.CREATED).body("Fare created successfully");
//        }

        @PostMapping("/create")
        public ResponseEntity<String> createFare(@RequestBody FareDto fareDto) {
            // First, check if the ride exists
            Optional<Ride> rideOptional = rideRepository.findById(fareDto.getRideId());
            if (rideOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ride not found");
            }

            Fare fare = new Fare();
            fare.setRide(rideOptional.get());
            fare.setAmount(fareDto.getAmount());
            fare.setStatus(fareDto.getStatus());

            try {
                fareRepository.save(fare);
                return ResponseEntity.status(HttpStatus.CREATED).body("Fare created successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating fare: " + e.getMessage());
            }
        }

        /**
         * Get a fare by ID
         * GET: /fares/{id}
         */
        @GetMapping("/{id}")
        public ResponseEntity<FareDto> getFareById(@PathVariable Long id) {
            Optional<Fare> fare = fareRepository.findById(id);
            if (fare.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                FareDto fareDto = new FareDto();
                fareDto.setRideId(fare.get().getRide().getId());
                fareDto.setAmount(fare.get().getAmount());
                fareDto.setStatus(fare.get().getStatus());
                return ResponseEntity.ok(fareDto);
            }
        }

        /**
         * Update an existing fare
         * PUT: /fares/update/{id}
         */
        @PutMapping("/update/{id}")
        public ResponseEntity<String> updateFare(@PathVariable Long id, @RequestBody FareDto fareDto) {
            Optional<Fare> fareOptional = fareRepository.findById(id);
            if (fareOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fare not found");
            }

            Fare fare = fareOptional.get();
            fare.setRide(rideRepository.findById(fareDto.getRideId()).orElse(null));
            fare.setAmount(fareDto.getAmount());
            fare.setStatus(fareDto.getStatus());

            fareRepository.save(fare);
            return ResponseEntity.ok("Fare updated successfully");
        }

        /**
         * Delete a fare by ID
         * DELETE: /fares/delete/{id}
         */
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteFare(@PathVariable Long id) {
            Optional<Fare> fareOptional = fareRepository.findById(id);
            if (fareOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fare not found");
            }

            fareRepository.delete(fareOptional.get());
            return ResponseEntity.ok("Fare deleted successfully");
        }
    }


