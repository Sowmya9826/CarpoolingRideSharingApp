package com.carpoolapp.carpoolService.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RideDtoTest {

    private RideDto rideDto;
    private final LocalDate testDate = LocalDate.of(2024, 1, 1);
    private final LocalTime startTime = LocalTime.of(9, 0);
    private final LocalTime endTime = LocalTime.of(17, 0);

    @BeforeEach
    void setUp() {
        rideDto = new RideDto();
        rideDto.setVehicleId(1L);
        rideDto.setStartLatitude(40.7128);
        rideDto.setStartLongitude(-74.0060);
        rideDto.setStartAddress("123 Start St");
        rideDto.setEndLatitude(40.7589);
        rideDto.setEndLongitude(-73.9851);
        rideDto.setEndAddress("456 End Ave");
        rideDto.setStartTime(startTime);
        rideDto.setEndTime(endTime);
        rideDto.setRecurring(true);
        rideDto.setDaysOfWeek("MON,TUE,WED");
        rideDto.setDate(testDate);
        rideDto.setCreatedDate(testDate);
    }

    @Test
    void testGettersAndSetters() {
        assertNotNull(rideDto);
        assertEquals(1L, rideDto.getVehicleId());
        assertEquals(40.7128, rideDto.getStartLatitude());
        assertEquals(-74.0060, rideDto.getStartLongitude());
        assertEquals("123 Start St", rideDto.getStartAddress());
        assertEquals(40.7589, rideDto.getEndLatitude());
        assertEquals(-73.9851, rideDto.getEndLongitude());
        assertEquals("456 End Ave", rideDto.getEndAddress());
        assertEquals(startTime, rideDto.getStartTime());
        assertEquals(endTime, rideDto.getEndTime());
        assertTrue(rideDto.isRecurring());
        assertEquals("MON,TUE,WED", rideDto.getDaysOfWeek());
        assertEquals(testDate, rideDto.getDate());
        assertEquals(testDate, rideDto.getCreatedDate());
    }

    @Test
    void testSettersAndGetters() {
        rideDto.setVehicleId(2L);
        rideDto.setStartLatitude(40.7589);
        rideDto.setStartLongitude(-73.9851);
        rideDto.setStartAddress("789 New Start St");
        rideDto.setEndLatitude(40.7128);
        rideDto.setEndLongitude(-74.0060);
        rideDto.setEndAddress("321 New End Ave");
        rideDto.setStartTime(LocalTime.of(10, 0));
        rideDto.setEndTime(LocalTime.of(18, 0));
        rideDto.setRecurring(false);
        rideDto.setDaysOfWeek("THU,FRI");
        rideDto.setDate(LocalDate.of(2024, 1, 2));
        rideDto.setCreatedDate(LocalDate.of(2024, 1, 2));

        assertEquals(2L, rideDto.getVehicleId());
        assertEquals(40.7589, rideDto.getStartLatitude());
        assertEquals(-73.9851, rideDto.getStartLongitude());
        assertEquals("789 New Start St", rideDto.getStartAddress());
        assertEquals(40.7128, rideDto.getEndLatitude());
        assertEquals(-74.0060, rideDto.getEndLongitude());
        assertEquals("321 New End Ave", rideDto.getEndAddress());
        assertEquals(LocalTime.of(10, 0), rideDto.getStartTime());
        assertEquals(LocalTime.of(18, 0), rideDto.getEndTime());
        assertFalse(rideDto.isRecurring());
        assertEquals("THU,FRI", rideDto.getDaysOfWeek());
        assertEquals(LocalDate.of(2024, 1, 2), rideDto.getDate());
        assertEquals(LocalDate.of(2024, 1, 2), rideDto.getCreatedDate());
    }

    @Test
    void testEqualsAndHashCode() {
        RideDto ride1 = new RideDto();
        ride1.setVehicleId(1L);
        ride1.setStartLatitude(40.7128);
        ride1.setStartLongitude(-74.0060);
        ride1.setStartAddress("123 Start St");
        ride1.setEndLatitude(40.7589);
        ride1.setEndLongitude(-73.9851);
        ride1.setEndAddress("456 End Ave");
        ride1.setStartTime(startTime);
        ride1.setEndTime(endTime);
        ride1.setRecurring(true);
        ride1.setDaysOfWeek("MON,TUE,WED");
        ride1.setDate(testDate);
        ride1.setCreatedDate(testDate);

        RideDto ride2 = new RideDto();
        ride2.setVehicleId(1L);
        ride2.setStartLatitude(40.7128);
        ride2.setStartLongitude(-74.0060);
        ride2.setStartAddress("123 Start St");
        ride2.setEndLatitude(40.7589);
        ride2.setEndLongitude(-73.9851);
        ride2.setEndAddress("456 End Ave");
        ride2.setStartTime(startTime);
        ride2.setEndTime(endTime);
        ride2.setRecurring(true);
        ride2.setDaysOfWeek("MON,TUE,WED");
        ride2.setDate(testDate);
        ride2.setCreatedDate(testDate);

        assertEquals(ride1, ride2);
        assertEquals(ride1.hashCode(), ride2.hashCode());

        ride2.setVehicleId(2L);
        assertNotEquals(ride1, ride2);
    }

    @Test
    void testToString() {
        String expectedString = "RideDto(vehicleId=1, startLatitude=40.7128, startLongitude=-74.006, startAddress=123 Start St, " +
                "endLatitude=40.7589, endLongitude=-73.9851, endAddress=456 End Ave, startTime=09:00, endTime=17:00, isRecurring=true, " +
                "daysOfWeek=MON,TUE,WED, date=2024-01-01, createdDate=2024-01-01)";
        assertEquals(expectedString, rideDto.toString());
    }
}
