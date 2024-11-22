package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.*;
import com.carpoolapp.carpoolService.models.*;
import com.carpoolapp.carpoolService.models.enums.*;
import com.carpoolapp.carpoolService.repository.*;
import com.carpoolapp.carpoolService.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
                LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalTime(),
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
                LocalDateTime.now().toLocalDate(),
                LocalDateTime.now().toLocalTime(),
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
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        // Check if the ride date is in the past
        if (rideDto.getDate() != null && rideDto.getDate().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("message", "Cannot book a ride for a past date");
            return "redirect:/rides/create";
        }



        double distance = fareService.calculateDistance(
                rideDto.getStartLatitude(), rideDto.getStartLongitude(),
                rideDto.getEndLatitude(), rideDto.getEndLongitude()
        );



        // Check if the distance exceeds the maximum allowed range (e.g., 100 km)
        final double MAX_DISTANCE = 100.0;
        if (distance > MAX_DISTANCE) {
            redirectAttributes.addFlashAttribute("message", "Cannot book a ride for such a long distance");
            return "redirect:/rides/create";
        }

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

        // Create a ride participant for the driver
        User driver = vehicleOptional.get().getOwner();
        rideParticipantService.createRideParticipantAsDriver(createdRide, driver);

        // Create a fare object for the ride
        fareService.createFare(createdRide);

        // if it is a recurring ride and the current date is one of the days of the week and start time is in the future,
        // create a one-time ride for the current date
        // create a ride participant for the driver
        // create a fare object for the ride
        // link the recurring ride with the one-time ride
        if (rideDto.getDaysOfWeek() != null && !rideDto.getDaysOfWeek().isEmpty()) {
            List<String> daysOfWeek = Arrays.asList(rideDto.getDaysOfWeek().split(","));

            if (daysOfWeek.contains(LocalDate.now().getDayOfWeek().name().substring(0, 3)) &&
                    createdRide.getEndTime().isAfter(LocalDateTime.now().toLocalTime())) {
                rideService.createOneTimeRideFromRecurringRide(createdRide.getId());
            }
        }

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

        // if the ride is recurring,
        // if there are any one-time rides that has date as the current date and start time in the future, or has a future date,
        // create a ride participant for the passenger in the one-time ride
        // decrease the available seats in the one-time ride
        // create a transaction for the new passenger in the one-time ride
        // update the transaction amount for all the passengers in the one-time ride
        if (ride.getType() == RideType.RECURRING) {
            List<Ride> oneTimeRides = rideRepository.findUpcomingOneTimeRidesForRecurringRide(ride.getId());
            for (Ride oneTimeRide : oneTimeRides) {
                rideParticipantService.createRideParticipantAsPassenger(oneTimeRide, passenger);

                // decrease the available seats in the one-time ride
                oneTimeRide.setAvailableSeats(oneTimeRide.getAvailableSeats() - 1);
                rideRepository.save(oneTimeRide);

                // create a transaction for the new passenger in the one-time ride
                transactionService.createTransactionForPassenger(oneTimeRide, passenger, TransactionType.CREDIT, "Ride fare");

                // update the transaction amount for all the passengers in the one-time ride
                transactionService.updateTransactionAmountOfPassengers(oneTimeRide);
            }
        }

        redirectAttributes.addFlashAttribute("message", "Joined the ride successfully!");

        return "redirect:/rides/";
    }

}