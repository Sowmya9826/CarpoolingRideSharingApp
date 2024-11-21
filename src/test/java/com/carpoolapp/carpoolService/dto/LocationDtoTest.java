package com.carpoolapp.carpoolService.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationDtoTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        LocationDto location = new LocationDto();
        double latitude = 40.7128;
        double longitude = -74.0060;
        String address = "New York, NY";

        // Act
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);

        // Assert
        assertEquals(latitude, location.getLatitude());
        assertEquals(longitude, location.getLongitude());
        assertEquals(address, location.getAddress());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        LocationDto location1 = new LocationDto();
        location1.setLatitude(40.7128);
        location1.setLongitude(-74.0060);
        location1.setAddress("New York, NY");

        LocationDto location2 = new LocationDto();
        location2.setLatitude(40.7128);
        location2.setLongitude(-74.0060);
        location2.setAddress("New York, NY");

        LocationDto differentLocation = new LocationDto();
        differentLocation.setLatitude(34.0522);
        differentLocation.setLongitude(-118.2437);
        differentLocation.setAddress("Los Angeles, CA");

        // Assert
        assertEquals(location1, location2);
        assertEquals(location1.hashCode(), location2.hashCode());
        assertNotEquals(location1, differentLocation);
    }

    @Test
    void testToString() {
        // Arrange
        LocationDto location = new LocationDto();
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);
        location.setAddress("New York, NY");

        // Act
        String result = location.toString();

        // Assert
        String expected = "LocationDto(latitude=40.7128, longitude=-74.006, address=New York, NY)";
        assertEquals(expected, result);
    }

    @Test
    void testNoArgsConstructor() {
        // Arrange & Act
        LocationDto location = new LocationDto();

        // Assert
        assertEquals(0.0, location.getLatitude());
        assertEquals(0.0, location.getLongitude());
        assertNull(location.getAddress());
    }
}