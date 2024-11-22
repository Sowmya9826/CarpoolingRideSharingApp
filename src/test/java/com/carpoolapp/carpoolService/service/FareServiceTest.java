package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.models.Fare;
import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.repository.FareRepository;
import com.carpoolapp.carpoolService.repository.RideParticipantRepository;
import com.carpoolapp.carpoolService.repository.RideRepository;
import com.carpoolapp.carpoolService.service.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareServiceTest {

    @Mock
    private FareRepository fareRepository;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideParticipantRepository rideParticipantRepository;

    @InjectMocks
    private FareService fareService;

    private Ride ride;
    private Location pickupLocation;
    private Location destinationLocation;
    private Fare fare;

    private static final double DELTA = 0.001; // Delta for double comparisons
    private static final double FARE_PER_KM = 1.0;

    @BeforeEach
    void setUp() {
        // Initialize test locations
        pickupLocation = new Location();
        pickupLocation.setLatitude(40.7128);  // New York
        pickupLocation.setLongitude(-74.0060);

        destinationLocation = new Location();
        destinationLocation.setLatitude(34.0522);  // Los Angeles
        destinationLocation.setLongitude(-118.2437);

        // Initialize test ride
        ride = new Ride();
        ride.setId(1L);
        ride.setPickupLocation(pickupLocation);
        ride.setDestinationLocation(destinationLocation);

        // Initialize test fare
        fare = new Fare();
        fare.setId(1L);
        fare.setAmount(100.0);
        fare.setRide(ride);
    }

    @Nested
    @DisplayName("Create Fare Tests")
    class CreateFareTests {

        @Test
        @DisplayName("Should successfully create fare for ride")
        void testCreateFare() {
            // Arrange
            ArgumentCaptor<Fare> fareCaptor = ArgumentCaptor.forClass(Fare.class);

            // Act
            fareService.createFare(ride);

            // Assert
            verify(fareRepository).save(fareCaptor.capture());
            Fare capturedFare = fareCaptor.getValue();

            assertNotNull(capturedFare);
            assertEquals(ride, capturedFare.getRide());
            assertTrue(capturedFare.getAmount() > 0);
        }

        @Test
        @DisplayName("Should handle ride with same pickup and destination")
        void testCreateFareWithSameLocations() {
            // Arrange
            ride.getDestinationLocation().setLatitude(ride.getPickupLocation().getLatitude());
            ride.getDestinationLocation().setLongitude(ride.getPickupLocation().getLongitude());

            // Act
            fareService.createFare(ride);

            // Assert
            verify(fareRepository).save(any(Fare.class));
        }
    }

    @Nested
    @DisplayName("Calculate Per Passenger Fare Tests")
    class CalculatePerPassengerFareTests {

        @Test
        @DisplayName("Should calculate fare per passenger correctly")
        void testCalculatePerPassengerFare() {
            // Arrange
            when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
            int passengerCount = 2;
            double expectedFare = fare.getAmount() / passengerCount;

            // Act
            double calculatedFare = fareService.calculatePerPassengerFare(ride, passengerCount);

            // Assert
            assertEquals(expectedFare, calculatedFare, DELTA);
        }

        @Test
        @DisplayName("Should handle zero passengers")
        void testCalculatePerPassengerFareWithZeroPassengers() {
            // Arrange
            when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));

            // Act
            double calculatedFare = fareService.calculatePerPassengerFare(ride, 0);

            // Assert
            assertEquals(fare.getAmount(), calculatedFare, DELTA);
        }

        @Test
        @DisplayName("Should throw exception when fare not found")
        void testCalculatePerPassengerFareWithNoFare() {
            // Arrange
            when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class, () ->
                    fareService.calculatePerPassengerFare(ride, 1)
            );
        }
    }

    @Nested
    @DisplayName("Get Estimated Fare Tests")
    class GetEstimatedFareTests {

        @Test
        @DisplayName("Should calculate estimated fare correctly")
        void testGetEstimatedFare() {
            // Arrange
            when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
            when(rideParticipantRepository.countActivePassengersInRide(ride.getId())).thenReturn(2);
            double expectedFare = fare.getAmount() / 3; // 2 existing passengers + 1 new

            // Act
            double estimatedFare = fareService.getEstimatedFare(ride.getId());

            // Assert
            assertEquals(expectedFare, estimatedFare, DELTA);
        }

        @Test
        @DisplayName("Should throw exception when fare not found")
        void testGetEstimatedFareWithNoFare() {
            // Arrange
            when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class, () ->
                    fareService.getEstimatedFare(ride.getId())
            );
        }
    }

    @Nested
    @DisplayName("Delete Fare Tests")
    class DeleteFareTests {

        @Test
        @DisplayName("Should successfully delete fare")
        void testDeleteFare() {
            // Arrange
            when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));

            // Act
            fareService.deleteFareForRide(ride);

            // Assert
            verify(fareRepository).delete(fare);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent fare")
        void testDeleteNonExistentFare() {
            // Arrange
            when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class, () ->
                    fareService.deleteFareForRide(ride)
            );
            verify(fareRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Distance Calculation Tests")
    class DistanceCalculationTests {

        @Test
        @DisplayName("Should calculate distance between two points correctly")
        void testCalculateDistance() {
            // Arrange
            double startLat = 40.7128; // New York
            double startLng = -74.0060;
            double endLat = 34.0522;   // Los Angeles
            double endLng = -118.2437;

            // Act
            double distance = fareService.calculateDistance(startLat, startLng, endLat, endLng);

            // Assert
            assertTrue(distance > 3900 && distance < 4000); // Approximate distance between NY and LA
        }

        @Test
        @DisplayName("Should return zero distance for same coordinates")
        void testCalculateDistanceForSamePoint() {
            // Arrange
            double lat = 40.7128;
            double lng = -74.0060;

            // Act
            double distance = fareService.calculateDistance(lat, lng, lat, lng);

            // Assert
            assertEquals(0.0, distance, DELTA);
        }

        @Test
        @DisplayName("Should calculate fare based on distance")
        void testCalculateFare() {
            // Arrange
            double startLat = 40.7128;
            double startLng = -74.0060;
            double endLat = 40.7589;
            double endLng = -73.9851;

            // Act
            double fare = fareService.calculateFare(startLat, startLng, endLat, endLng);
            double distance = fareService.calculateDistance(startLat, startLng, endLat, endLng);

            // Assert
            assertEquals(distance * FARE_PER_KM, fare, DELTA);
        }
    }
}