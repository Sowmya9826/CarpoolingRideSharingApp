package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.*;
import com.carpoolapp.carpoolService.models.*;
import com.carpoolapp.carpoolService.models.enums.*;
import com.carpoolapp.carpoolService.respository.*;
import com.carpoolapp.carpoolService.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rides")
public class RideController {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private RideService rideService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Autowired
    private FareService fareService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RideParticipantService rideParticipantService;

    @Autowired
    private RideParticipantRepository rideParticipantRepository;


    @GetMapping("/")
    public String showRidesPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        List<UserRideInfoDto> upcomingRides = rideParticipantRepository.findUpcomingRidesForUser(
                userId,
                LocalDateTime.now(ZoneId.of("UTC")).toLocalDate(),
                LocalDateTime.now(ZoneId.of("UTC")).toLocalTime(),
                RideParticipantStatus.ACTIVE
        );

        List<UserRideInfoDto> recurringRides = rideParticipantRepository.findRecurringRidesForUser(
                userId,
                RideParticipantStatus.ACTIVE
        );

        // Preprocess the daysOfWeek into a list
        for (UserRideInfoDto rideInfo : recurringRides) {
            if (rideInfo.getDaysOfWeek() != null && !rideInfo.getDaysOfWeek().isEmpty()) {
                rideInfo.setDaysOfWeekList(Arrays.asList(rideInfo.getDaysOfWeek().split(",")));
            }
        }

        List<UserRideInfoDto> pastRides = rideParticipantRepository.findPastRidesForUser(
                userId,
                LocalDateTime.now(ZoneId.of("UTC")).toLocalDate(),
                LocalDateTime.now(ZoneId.of("UTC")).toLocalTime(),
                RideParticipantStatus.ACTIVE
        );

        model.addAttribute("upcomingRides", upcomingRides);
        model.addAttribute("recurringRides", recurringRides);
        model.addAttribute("pastRides", pastRides);

        return "rides/show_rides";
    }


    @GetMapping("/{id}")
    public String showRideDetails(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isEmpty()) {
            return "redirect:/rides/";
        }

        Ride ride = rideOptional.get();
        Vehicle vehicle = ride.getVehicle();
        Location startLocation = ride.getPickupLocation();
        Location endLocation = ride.getDestinationLocation();
        List<RideParticipantDto> rideParticipants = rideParticipantRepository.getActiveRidePassengersDetails(id);
        userService.addProfileImagesToRideParticipantDto(rideParticipants);

        model.addAttribute("ride", ride);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("startLocation", startLocation);
        model.addAttribute("endLocation", endLocation);
        model.addAttribute("rideParticipants", rideParticipants);

        return "rides/view_ride_details";
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

        // create a fare object for the ride
        fareService.createFare(createdRide);

        redirectAttributes.addFlashAttribute("message", "Ride created successfully!");

        return "redirect:/rides/";
    }


    @PostMapping("/{id}/cancel")
    public String cancelRide(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isEmpty()) {
            return "redirect:/rides/";
        }

        // if the user is the driver, cancel the ride and all the participants, else cancel the user from the ride
        Ride ride = rideOptional.get();
        if (ride.getVehicle().getOwner().getId().equals(userId)) {
            ride.setStatus(RideStatus.CANCELLED);
            rideRepository.save(ride);
            rideParticipantService.markRideParticipantsAsCancelled(ride);

            // delete all the transactions
            transactionService.deleteTransactionsForRide(ride);

            // delete the fare
            fareService.deleteFareForRide(ride);
        } else {
            rideParticipantService.markRideParticipantAsCancelled(ride, userId);

            // increase the available seats in the ride
            ride.setAvailableSeats(ride.getAvailableSeats() + 1);
            rideRepository.save(ride);

            // delete the transaction for the user
            transactionService.deleteTransactionForUserInRide(userId, ride);

            // update the transaction amount for all the passengers
            transactionService.updateTransactionAmountOfPassengers(ride);
        }

        redirectAttributes.addFlashAttribute("message", "Ride cancelled successfully!");

        return "redirect:/rides/";
    }


    @GetMapping("/find")
    public String showFindRidesPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        return "rides/find_rides_form";
    }


    @PostMapping("/find")
    public String findMatchingRides(FindRideDto findRideDto, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        List<MatchingRideDto> matchingRides = rideService.findMatchingRidesByEndTimeAndProximity(
                userId,
                findRideDto.isRecurring(),
                findRideDto.getDate(),
                findRideDto.getEndTime(),
                findRideDto.getStartLatitude(),
                findRideDto.getStartLongitude(),
                findRideDto.getEndLatitude(),
                findRideDto.getEndLongitude(),
                2.0
        );

        // Preprocess the daysOfWeek into a list
        for (MatchingRideDto rideInfo : matchingRides) {
            if (rideInfo.getDaysOfWeek() != null && !rideInfo.getDaysOfWeek().isEmpty()) {
                rideInfo.setDaysOfWeekList(Arrays.asList(rideInfo.getDaysOfWeek().split(",")));
            }
        }

        // Add the estimated fare to the matching rides
        for (MatchingRideDto ride : matchingRides) {
            // round the fare to 2 decimal places
            ride.setEstimatedFare(Math.round(fareService.getEstimatedFare(ride.getRideId()) * 100.0) / 100.0);
        }

        model.addAttribute("matchingRides", matchingRides);

        return "rides/show_matching_rides";
    }


    @PostMapping("/{id}/join")
    public String joinRide(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isEmpty()) {
            return "redirect:/rides/";
        }

        Ride ride = rideOptional.get();

        // create a ride participant for the passenger
        User passenger = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        rideParticipantService.createRideParticipantAsPassenger(ride, passenger);

        // decrease the available seats in the ride
        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideRepository.save(ride);

        // create a transaction for the new passenger
        transactionService.createTransactionForPassenger(ride, passenger, TransactionType.CREDIT, "Ride fare");

        // update the transaction amount for all the passengers
        transactionService.updateTransactionAmountOfPassengers(ride);

        redirectAttributes.addFlashAttribute("message", "Joined the ride successfully!");

        return "redirect:/rides/";
    }

}