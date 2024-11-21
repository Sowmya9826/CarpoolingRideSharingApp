package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.dto.MatchingRideDto;
import com.carpoolapp.carpoolService.dto.RideDto;
import com.carpoolapp.carpoolService.models.*;
import com.carpoolapp.carpoolService.models.enums.RideStatus;
import com.carpoolapp.carpoolService.models.enums.RideType;
import com.carpoolapp.carpoolService.repository.LocationRepository;
import com.carpoolapp.carpoolService.repository.RideParticipantRepository;
import com.carpoolapp.carpoolService.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideParticipantRepository rideParticipantRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RideService rideService;

    private RideDto rideDto;
    private Vehicle vehicle;
    private Location startLocation;
    private Location endLocation;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(rideService, "apiKey", "test-api-key");

        rideDto = new RideDto();
        rideDto.setStartTime(LocalTime.of(9, 0));
        rideDto.setStartLatitude(40.7128);
        rideDto.setStartLongitude(-74.0060);
        rideDto.setEndLatitude(40.7589);
        rideDto.setEndLongitude(-73.9851);
        rideDto.setDate(LocalDate.now());

        vehicle = new Vehicle();
        vehicle.setSeatCount(4);

        startLocation = new Location();
        startLocation.setLatitude(40.7128);
        startLocation.setLongitude(-74.0060);

        endLocation = new Location();
        endLocation.setLatitude(40.7589);
        endLocation.setLongitude(-73.9851);
    }

    @Test
    void calculateEndTime_WithValidResponse_ShouldReturnCorrectTime() {
        // Arrange
        String mockResponse = """
            {
                "rows": [{
                    "elements": [{
                        "duration": {
                            "value": 1800
                        }
                    }]
                }]
            }
            """;
        when(restTemplate.getForObject(any(String.class), eq(String.class)))
                .thenReturn(mockResponse);

        // Act
        LocalTime endTime = rideService.calculateEndTime(
                rideDto.getStartLatitude(),
                rideDto.getStartLongitude(),
                rideDto.getEndLatitude(),
                rideDto.getEndLongitude(),
                rideDto.getStartTime()
        );


    }

    @Test
    void calculateEndTime_WithErrorResponse_ShouldReturnDefaultTime() {
        // Arrange
        when(restTemplate.getForObject(any(String.class), eq(String.class)))
                .thenReturn(null);

        // Act
        LocalTime endTime = rideService.calculateEndTime(
                rideDto.getStartLatitude(),
                rideDto.getStartLongitude(),
                rideDto.getEndLatitude(),
                rideDto.getEndLongitude(),
                rideDto.getStartTime()
        );


        // Verify
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(String.class));
    }


    @Test
    void createRide_OneTimeRide_ShouldCreateRideCorrectly() {
        // Arrange
        ArgumentCaptor<Ride> rideCaptor = ArgumentCaptor.forClass(Ride.class);
        when(rideRepository.save(any(Ride.class))).thenReturn(new Ride());

        // Act
        Ride createdRide = rideService.createRide(rideDto, vehicle, startLocation, endLocation);

        // Assert
        verify(rideRepository).save(rideCaptor.capture());
        Ride capturedRide = rideCaptor.getValue();

        assertEquals(RideStatus.CREATED, capturedRide.getStatus());
        assertEquals(RideType.ONE_TIME, capturedRide.getType());
        assertEquals(vehicle.getSeatCount() - 1, capturedRide.getAvailableSeats());
        assertEquals(rideDto.getDate(), capturedRide.getDate());
        assertNotNull(capturedRide.getCreatedDate());
    }

    @Test
    void createRide_RecurringRide_ShouldCreateRideCorrectly() {
        // Arrange
        rideDto.setDate(null);
        rideDto.setDaysOfWeek("1,2,3");
        ArgumentCaptor<Ride> rideCaptor = ArgumentCaptor.forClass(Ride.class);
        when(rideRepository.save(any(Ride.class))).thenReturn(new Ride());

        // Act
        Ride createdRide = rideService.createRide(rideDto, vehicle, startLocation, endLocation);

        // Assert
        verify(rideRepository).save(rideCaptor.capture());
        Ride capturedRide = rideCaptor.getValue();

        assertEquals(RideType.RECURRING, capturedRide.getType());
        assertEquals("1,2,3", capturedRide.getDaysOfWeek());
    }

    @Test
    void findMatchingRidesByEndTimeAndProximity_ShouldFilterByTimeAndDistance() {
        // Arrange
        LocalDate date = LocalDate.now();
        LocalTime userEndTime = LocalTime.of(17, 0);
        double userStartLat = 40.7128;
        double userStartLon = -74.0060;
        double userEndLat = 40.7589;
        double userEndLon = -73.9851;

        // Create MatchingRideDtos for testing
        List<MatchingRideDto> mockRides = Arrays.asList(
                new MatchingRideDto(
                        1L,                         // rideId
                        LocalDate.now(),            // date
                        "Driver 1",                 // driverName
                        LocalTime.of(16, 45),       // startTime
                        LocalTime.of(17, 15),       // endTime
                        RideStatus.CREATED,         // status
                        3,                          // availableSeats
                        "Toyota Camry",             // vehicleName
                        userStartLat,               // startLatitude (nearby)
                        userStartLon,               // startLongitude
                        "Downtown",                 // startLocationName
                        userEndLat,                 // endLatitude (nearby)
                        userEndLon                  // endLongitude
                ),
                new MatchingRideDto(
                        2L,                         // rideId
                        LocalDate.now(),            // date
                        "Driver 2",                 // driverName
                        LocalTime.of(16, 45),       // startTime
                        LocalTime.of(17, 15),       // endTime
                        RideStatus.CREATED,         // status
                        3,                          // availableSeats
                        "Honda Civic",              // vehicleName
                        42.0,                       // startLatitude (far)
                        -75.0,                      // startLongitude
                        "Far Location",             // startLocationName
                        42.1,                       // endLatitude (far)
                        -75.1                       // endLongitude
                )
        );

        // Mock repository response
        when(rideRepository.findOneTimeRidesWithLocationsExcludingUser(
                eq(date),
                any(LocalTime.class),
                any(LocalTime.class),
                eq(1L)
        )).thenReturn(mockRides);

        // Act
        List<MatchingRideDto> matches = rideService.findMatchingRidesByEndTimeAndProximity(
                1L, false, date, userEndTime,
                userStartLat, userStartLon,
                userEndLat, userEndLon,
                5.0
        );

        // Assert
        assertEquals(1, matches.size());
        MatchingRideDto match = matches.get(0);
        assertEquals(1L, match.getRideId());
        assertEquals(userStartLat, match.getStartLatitude(), 0.0001);
        assertEquals(userStartLon, match.getStartLongitude(), 0.0001);
        assertEquals(userEndLat, match.getEndLatitude(), 0.0001);
        assertEquals(userEndLon, match.getEndLongitude(), 0.0001);
    }
//
}