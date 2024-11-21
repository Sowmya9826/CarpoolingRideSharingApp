package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.service.FareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FareController {
    @Autowired
    private  FareService fareService;

    /**
     * Endpoint to calculate the fare based on start and destination coordinates.
     * @param startLatitude  Latitude of the starting point
     * @param startLongitude Longitude of the starting point
     * @param endLatitude    Latitude of the destination
     * @param endLongitude   Longitude of the destination
     * @return               Estimated fare
     */
    @GetMapping("/fares/estimateFare")
    public double estimateFare(@RequestParam double startLatitude,
                               @RequestParam double startLongitude,
                               @RequestParam double endLatitude,
                               @RequestParam double endLongitude) {
        return fareService.calculateFare(startLatitude, startLongitude, endLatitude, endLongitude);
    }
}


//
//import com.carpoolapp.carpoolService.dto.FareDto;
//import com.carpoolapp.carpoolService.models.Fare;
//import com.carpoolapp.carpoolService.service.FareService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/fares")
//public class FareController {
//
//    @Autowired
//    private FareService fareService;  // Inject FareService
//
//    /**
//     * Create a new fare
//     * POST: /fares/create
//     */
//    @PostMapping("/create")
//    public ResponseEntity<String> createFare(@RequestBody FareDto fareDto) {
//        try {
//            String responseMessage = fareService.createFare(fareDto);
//            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating fare: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Estimate the fare based on the coordinates.
//     * GET: /fares/estimateFare
//     */
//    @GetMapping("/estimateFare")
//    public ResponseEntity<Double> estimateFare(@RequestParam double startLatitude,
//                                               @RequestParam double startLongitude,
//                                               @RequestParam double endLatitude,
//                                               @RequestParam double endLongitude) {
//        double fare = fareService.calculateFare(startLatitude, startLongitude, endLatitude, endLongitude);
//        return ResponseEntity.ok(fare);
//    }
//
//    /**
//     * Delete a fare by ID
//     * DELETE: /fares/delete/{id}
//     */
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteFare(@PathVariable Long id) {
//        boolean deleted = fareService.deleteFareById(id);
//        if (deleted) {
//            return ResponseEntity.ok("Fare deleted successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fare not found");
//        }
//    }
//}