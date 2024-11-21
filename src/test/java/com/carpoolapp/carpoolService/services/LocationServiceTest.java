package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private static final double VALID_LATITUDE = 40.7128;
    private static final double VALID_LONGITUDE = -74.0060;
    private static final String VALID_ADDRESS = "New York, NY";

    @Nested
    @DisplayName("Create Location Tests")
    class CreateLocationTests {

        @Test
        @DisplayName("Should successfully create location with valid inputs")
        void testCreateLocationSuccess() {
            // Arrange
            Location expectedLocation = new Location();
            expectedLocation.setLatitude(VALID_LATITUDE);
            expectedLocation.setLongitude(VALID_LONGITUDE);
            expectedLocation.setAddress(VALID_ADDRESS);

            when(locationRepository.save(any(Location.class))).thenReturn(expectedLocation);

            // Act
            Location createdLocation = locationService.createLocation(VALID_LATITUDE, VALID_LONGITUDE, VALID_ADDRESS);

            // Assert
            assertNotNull(createdLocation);
            assertEquals(VALID_LATITUDE, createdLocation.getLatitude());
            assertEquals(VALID_LONGITUDE, createdLocation.getLongitude());
            assertEquals(VALID_ADDRESS, createdLocation.getAddress());

            // Verify repository interaction
            verify(locationRepository, times(1)).save(any(Location.class));
        }

        @Test
        @DisplayName("Should save location with correct values")
        void testLocationValuesPersistence() {
            // Arrange
            ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);

            // Act
            locationService.createLocation(VALID_LATITUDE, VALID_LONGITUDE, VALID_ADDRESS);

            // Assert
            verify(locationRepository).save(locationCaptor.capture());
            Location capturedLocation = locationCaptor.getValue();

            assertEquals(VALID_LATITUDE, capturedLocation.getLatitude());
            assertEquals(VALID_LONGITUDE, capturedLocation.getLongitude());
            assertEquals(VALID_ADDRESS, capturedLocation.getAddress());
        }

        @Test
        @DisplayName("Should handle edge case with minimum latitude value")
        void testCreateLocationWithMinLatitude() {
            // Arrange
            double minLatitude = -90.0;

            // Act
            Location location = locationService.createLocation(minLatitude, VALID_LONGITUDE, VALID_ADDRESS);

            // Assert
            assertNotNull(location);
            assertEquals(minLatitude, location.getLatitude());
            verify(locationRepository, times(1)).save(any(Location.class));
        }

        @Test
        @DisplayName("Should handle edge case with maximum latitude value")
        void testCreateLocationWithMaxLatitude() {
            // Arrange
            double maxLatitude = 90.0;

            // Act
            Location location = locationService.createLocation(maxLatitude, VALID_LONGITUDE, VALID_ADDRESS);

            // Assert
            assertNotNull(location);
            assertEquals(maxLatitude, location.getLatitude());
            verify(locationRepository, times(1)).save(any(Location.class));
        }

        @Test
        @DisplayName("Should handle edge case with minimum longitude value")
        void testCreateLocationWithMinLongitude() {
            // Arrange
            double minLongitude = -180.0;

            // Act
            Location location = locationService.createLocation(VALID_LATITUDE, minLongitude, VALID_ADDRESS);

            // Assert
            assertNotNull(location);
            assertEquals(minLongitude, location.getLongitude());
            verify(locationRepository, times(1)).save(any(Location.class));
        }

        @Test
        @DisplayName("Should handle edge case with maximum longitude value")
        void testCreateLocationWithMaxLongitude() {
            // Arrange
            double maxLongitude = 180.0;

            // Act
            Location location = locationService.createLocation(VALID_LATITUDE, maxLongitude, VALID_ADDRESS);

            // Assert
            assertNotNull(location);
            assertEquals(maxLongitude, location.getLongitude());
            verify(locationRepository, times(1)).save(any(Location.class));
        }

        @Test
        @DisplayName("Should handle empty address")
        void testCreateLocationWithEmptyAddress() {
            // Arrange
            String emptyAddress = "";

            // Act
            Location location = locationService.createLocation(VALID_LATITUDE, VALID_LONGITUDE, emptyAddress);

            // Assert
            assertNotNull(location);
            assertEquals(emptyAddress, location.getAddress());
            verify(locationRepository, times(1)).save(any(Location.class));
        }

        @Test
        @DisplayName("Should handle null address")
        void testCreateLocationWithNullAddress() {
            // Act
            Location location = locationService.createLocation(VALID_LATITUDE, VALID_LONGITUDE, null);

            // Assert
            assertNotNull(location);
            assertNull(location.getAddress());
            verify(locationRepository, times(1)).save(any(Location.class));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle repository save failure")
        void testRepositorySaveFailure() {
            // Arrange
            when(locationRepository.save(any(Location.class))).thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () ->
                    locationService.createLocation(VALID_LATITUDE, VALID_LONGITUDE, VALID_ADDRESS)
            );
            verify(locationRepository, times(1)).save(any(Location.class));
        }
    }
}