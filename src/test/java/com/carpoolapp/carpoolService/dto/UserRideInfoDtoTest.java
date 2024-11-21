package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserRideInfoDtoTest {

    private UserRideInfoDto userRideInfoDto;

    private final Long rideId = 1L;
    private final LocalDate date = LocalDate.of(2024, 1, 1);
    private final String daysOfWeek = "MON,TUE,WED";
    private final LocalTime startTime = LocalTime.of(9, 0);
    private final LocalTime endTime = LocalTime.of(17, 0);
    private final Location pickupLocation = new Location();  // Adjust if you need a real Location object
    private final Location destinationLocation = new Location();  // Adjust if you need a real Location object
    private final RideParticipateRole role = RideParticipateRole.PASSENGER;

    @BeforeEach
    void setUp() {
        userRideInfoDto = new UserRideInfoDto(
                rideId,
                date,
                daysOfWeek,
                startTime,
                endTime,
                pickupLocation,
                destinationLocation,
                role
        );
    }

    @Test
    void testGettersAndSetters() {
        // Test getters
        assertEquals(rideId, userRideInfoDto.getRideId());
        assertEquals(date, userRideInfoDto.getDate());
        assertEquals(daysOfWeek, userRideInfoDto.getDaysOfWeek());
        assertEquals(startTime, userRideInfoDto.getStartTime());
        assertEquals(endTime, userRideInfoDto.getEndTime());
        assertEquals(pickupLocation, userRideInfoDto.getPickupLocation());
        assertEquals(destinationLocation, userRideInfoDto.getDestinationLocation());
        assertEquals(role, userRideInfoDto.getRole());

        // Test setters
        userRideInfoDto.setRideId(2L);
        userRideInfoDto.setDate(LocalDate.of(2024, 2, 1));
        userRideInfoDto.setDaysOfWeek("THU,FRI");
        userRideInfoDto.setStartTime(LocalTime.of(10, 0));
        userRideInfoDto.setEndTime(LocalTime.of(18, 0));
        userRideInfoDto.setPickupLocation(new Location());
        userRideInfoDto.setDestinationLocation(new Location());
        userRideInfoDto.setRole(RideParticipateRole.DRIVER);

        assertEquals(2L, userRideInfoDto.getRideId());
        assertEquals(LocalDate.of(2024, 2, 1), userRideInfoDto.getDate());
        assertEquals("THU,FRI", userRideInfoDto.getDaysOfWeek());
        assertEquals(LocalTime.of(10, 0), userRideInfoDto.getStartTime());
        assertEquals(LocalTime.of(18, 0), userRideInfoDto.getEndTime());
        assertEquals(new Location(), userRideInfoDto.getPickupLocation());
        assertEquals(new Location(), userRideInfoDto.getDestinationLocation());
        assertEquals(RideParticipateRole.DRIVER, userRideInfoDto.getRole());
    }

    @Test
    void testConstructor() {
        // Test constructor values are set correctly
        assertNotNull(userRideInfoDto);
        assertEquals(rideId, userRideInfoDto.getRideId());
        assertEquals(date, userRideInfoDto.getDate());
        assertEquals(daysOfWeek, userRideInfoDto.getDaysOfWeek());
        assertEquals(startTime, userRideInfoDto.getStartTime());
        assertEquals(endTime, userRideInfoDto.getEndTime());
        assertEquals(pickupLocation, userRideInfoDto.getPickupLocation());
        assertEquals(destinationLocation, userRideInfoDto.getDestinationLocation());
        assertEquals(role, userRideInfoDto.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create another UserRideInfoDto with the same values
        UserRideInfoDto userRideInfoDto2 = new UserRideInfoDto(
                rideId, date, daysOfWeek, startTime, endTime, pickupLocation, destinationLocation, role
        );

        // Test equality
        assertEquals(userRideInfoDto, userRideInfoDto2);
        assertEquals(userRideInfoDto.hashCode(), userRideInfoDto2.hashCode());

        // Modify userRideInfoDto2 and test inequality
        userRideInfoDto2.setRideId(2L);
        assertNotEquals(userRideInfoDto, userRideInfoDto2);
    }

    @Test
    void testToString() {
        String expectedString = "UserRideInfoDto(rideId=" + rideId +
                ", date=" + date +
                ", daysOfWeek=" + daysOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pickupLocation=" + pickupLocation +
                ", destinationLocation=" + destinationLocation +
                ", role=" + role +
                ", daysOfWeekList=null)";

        // Verify toString() contains expected values
        assertTrue(userRideInfoDto.toString().contains("rideId=" + rideId));
        assertTrue(userRideInfoDto.toString().contains("date=" + date));
        assertTrue(userRideInfoDto.toString().contains("daysOfWeek=" + daysOfWeek));
        assertTrue(userRideInfoDto.toString().contains("startTime=" + startTime));
        assertTrue(userRideInfoDto.toString().contains("endTime=" + endTime));
        assertTrue(userRideInfoDto.toString().contains("pickupLocation=" + pickupLocation));
        assertTrue(userRideInfoDto.toString().contains("destinationLocation=" + destinationLocation));
        assertTrue(userRideInfoDto.toString().contains("role=" + role));
    }


}
