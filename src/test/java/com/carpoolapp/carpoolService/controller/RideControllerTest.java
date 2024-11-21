package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.repository.RideRepository;
import com.carpoolapp.carpoolService.repository.UserRepository;
import com.carpoolapp.carpoolService.service.RideParticipantService;
import com.carpoolapp.carpoolService.service.TransactionService;
import com.carpoolapp.carpoolService.models.enums.TransactionType;
import com.carpoolapp.carpoolService.dto.FindRideDto;
import com.carpoolapp.carpoolService.dto.MatchingRideDto;
import com.carpoolapp.carpoolService.models.enums.RideStatus;
import com.carpoolapp.carpoolService.service.RideService;
import com.carpoolapp.carpoolService.service.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RideControllerTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RideParticipantService rideParticipantService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private RideService rideService;

    @Mock
    private FareService fareService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private RideController rideController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindMatchingRides() {
        Long userId = 1L;
        FindRideDto findRideDto = new FindRideDto();
        findRideDto.setRecurring(false);
        findRideDto.setDate(LocalDate.parse("2023-10-10"));
        findRideDto.setEndTime(LocalTime.parse("18:00"));
        findRideDto.setStartLatitude(12.9716);
        findRideDto.setStartLongitude(77.5946);
        findRideDto.setEndLatitude(13.0827);
        findRideDto.setEndLongitude(80.2707);

        when(session.getAttribute("userId")).thenReturn(userId);

        MatchingRideDto matchingRide = new MatchingRideDto(
                1L,
                LocalDate.parse("2023-10-10"),
                "Mon,Tue",
                LocalTime.parse("08:00"),
                LocalTime.parse("18:00"),
                RideStatus.ACTIVE,
                3,
                "Start Address",
                12.9716,
                77.5946,
                "End Address",
                13.0827,
                80.2707
        );

        List<MatchingRideDto> matchingRides = Arrays.asList(matchingRide);

        when(rideService.findMatchingRidesByEndTimeAndProximity(
                anyLong(), anyBoolean(), any(LocalDate.class), any(LocalTime.class),
                anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(matchingRides);

        when(fareService.getEstimatedFare(anyLong())).thenReturn(100.0);

        String viewName = rideController.findMatchingRides(findRideDto, session, model);

        assertEquals("rides/show_matching_rides", viewName);
        verify(model).addAttribute("matchingRides", matchingRides);
    }

    @Test
    void testJoinRide() {
        Long userId = 1L;
        Long rideId = 1L;

        when(session.getAttribute("userId")).thenReturn(userId);

        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setAvailableSeats(3);
        Vehicle vehicle = new Vehicle();
        User driver = new User();
        driver.setId(2L);
        vehicle.setOwner(driver);
        ride.setVehicle(vehicle);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        User passenger = new User();
        passenger.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(passenger));

        String viewName = rideController.joinRide(rideId, session, redirectAttributes);

        assertEquals("redirect:/rides/", viewName);

        verify(rideParticipantService).createRideParticipantAsPassenger(ride, passenger);
        verify(rideRepository).save(ride);
        verify(transactionService).createTransactionForPassenger(ride, passenger, TransactionType.CREDIT, "Ride fare");
        verify(transactionService).updateTransactionAmountOfPassengers(ride);
        assertEquals(2, ride.getAvailableSeats());
    }
}