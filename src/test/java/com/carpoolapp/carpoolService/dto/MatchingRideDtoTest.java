package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.enums.RideStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MatchingRideDtoTest {
    private MatchingRideDto matchingRide;
    private final LocalDate testDate = LocalDate.of(2024, 1, 1);
    private final LocalTime startTime = LocalTime.of(9, 0);
    private final LocalTime endTime = LocalTime.of(17, 0);

    @BeforeEach
    void setUp() {
        // Ensure the parameters match the constructor signature
        matchingRide = new MatchingRideDto(
                1L,                         // rideId (Long, not long)
                testDate,                   // date
                "MON,TUE,WED",              // daysOfWeek
                startTime,                  // startTime
                endTime,                    // endTime
                RideStatus.ACTIVE,          // status
                4,                          // availableSeats
                "123 Start St",             // startAddress
                40.7128,                    // startLatitude
                -74.0060,                   // startLongitude
                "456 End Ave",              // endAddress
                40.7589,                    // endLatitude
                -73.9851                    // endLongitude
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(matchingRide);
        assertEquals(1L, matchingRide.getRideId());  // Ensure Long (1L) is passed, not long
        assertEquals(testDate, matchingRide.getDate());
        assertEquals(startTime, matchingRide.getStartTime());
        assertEquals(endTime, matchingRide.getEndTime());
        assertEquals(RideStatus.ACTIVE, matchingRide.getStatus());
        assertEquals(4, matchingRide.getAvailableSeats());
        assertEquals("123 Start St", matchingRide.getStartAddress());
        assertEquals(40.7128, matchingRide.getStartLatitude());
        assertEquals(-74.0060, matchingRide.getStartLongitude());
        assertEquals("456 End Ave", matchingRide.getEndAddress());
        assertEquals(40.7589, matchingRide.getEndLatitude());
        assertEquals(-73.9851, matchingRide.getEndLongitude());
    }

    @Test
    void testSettersAndGetters() {
        matchingRide.setRideId(2L);
        matchingRide.setDate(LocalDate.of(2024, 1, 2));
        matchingRide.setStartTime(LocalTime.of(10, 0));
        matchingRide.setEndTime(LocalTime.of(18, 0));
        matchingRide.setStatus(RideStatus.COMPLETED);
        matchingRide.setAvailableSeats(3);
        matchingRide.setStartAddress("789 New Start St");
        matchingRide.setStartLatitude(40.7589);
        matchingRide.setStartLongitude(-73.9851);
        matchingRide.setEndAddress("321 New End Ave");
        matchingRide.setEndLatitude(40.7128);
        matchingRide.setEndLongitude(-74.0060);
        matchingRide.setEstimatedFare(25.50);

        assertEquals(2L, matchingRide.getRideId());
        assertEquals(LocalDate.of(2024, 1, 2), matchingRide.getDate());
        assertEquals(LocalTime.of(10, 0), matchingRide.getStartTime());
        assertEquals(LocalTime.of(18, 0), matchingRide.getEndTime());
        assertEquals(RideStatus.COMPLETED, matchingRide.getStatus());
        assertEquals(3, matchingRide.getAvailableSeats());
        assertEquals("789 New Start St", matchingRide.getStartAddress());
        assertEquals(40.7589, matchingRide.getStartLatitude());
        assertEquals(-73.9851, matchingRide.getStartLongitude());
        assertEquals("321 New End Ave", matchingRide.getEndAddress());
        assertEquals(40.7128, matchingRide.getEndLatitude());
        assertEquals(-74.0060, matchingRide.getEndLongitude());
    }




}
