package com.carpoolapp.carpoolService.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class FindRideDtoTest {

    @Test
    void testDefaultConstructor() {
        // Act
        FindRideDto dto = new FindRideDto();

        // Assert
        assertNotNull(dto);
        assertEquals(0.0, dto.getStartLatitude());
        assertEquals(0.0, dto.getStartLongitude());
        assertEquals(0.0, dto.getEndLatitude());
        assertEquals(0.0, dto.getEndLongitude());
        assertNull(dto.getDate());
        assertNull(dto.getEndTime());
        assertFalse(dto.isRecurring());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        FindRideDto dto = new FindRideDto();
        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime time = LocalTime.of(14, 30);

        // Act
        dto.setStartLatitude(12.345);
        dto.setStartLongitude(67.890);
        dto.setEndLatitude(23.456);
        dto.setEndLongitude(78.901);
        dto.setDate(date);
        dto.setEndTime(time);
        dto.setRecurring(true);

        // Assert
        assertEquals(12.345, dto.getStartLatitude());
        assertEquals(67.890, dto.getStartLongitude());
        assertEquals(23.456, dto.getEndLatitude());
        assertEquals(78.901, dto.getEndLongitude());
        assertEquals(date, dto.getDate());
        assertEquals(time, dto.getEndTime());
        assertTrue(dto.isRecurring());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        FindRideDto dto1 = new FindRideDto();
        FindRideDto dto2 = new FindRideDto();
        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime time = LocalTime.of(14, 30);

        dto1.setStartLatitude(12.345);
        dto1.setStartLongitude(67.890);
        dto1.setEndLatitude(23.456);
        dto1.setEndLongitude(78.901);
        dto1.setDate(date);
        dto1.setEndTime(time);
        dto1.setRecurring(true);

        dto2.setStartLatitude(12.345);
        dto2.setStartLongitude(67.890);
        dto2.setEndLatitude(23.456);
        dto2.setEndLongitude(78.901);
        dto2.setDate(date);
        dto2.setEndTime(time);
        dto2.setRecurring(true);

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        // Arrange
        FindRideDto dto1 = new FindRideDto();
        FindRideDto dto2 = new FindRideDto();

        dto1.setStartLatitude(12.345);
        dto2.setStartLatitude(12.346); // Different value

        // Assert
        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        FindRideDto dto = new FindRideDto();
        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime time = LocalTime.of(14, 30);

        dto.setStartLatitude(12.345);
        dto.setStartLongitude(67.890);
        dto.setEndLatitude(23.456);
        dto.setEndLongitude(78.901);
        dto.setDate(date);
        dto.setEndTime(time);
        dto.setRecurring(true);

        // Act
        String toString = dto.toString();

        // Assert
        assertTrue(toString.contains("startLatitude=12.345"));
        assertTrue(toString.contains("startLongitude=67.89"));
        assertTrue(toString.contains("endLatitude=23.456"));
        assertTrue(toString.contains("endLongitude=78.901"));
        assertTrue(toString.contains("date=" + date));
        assertTrue(toString.contains("endTime=" + time));
        assertTrue(toString.contains("isRecurring=true"));
    }

    @Test
    void testNullValues() {
        // Arrange
        FindRideDto dto = new FindRideDto();

        // Act & Assert
        assertDoesNotThrow(() -> {
            dto.setDate(null);
            dto.setEndTime(null);
        });

        assertNull(dto.getDate());
        assertNull(dto.getEndTime());
    }

    @Test
    void testBoundaryValues() {
        // Arrange
        FindRideDto dto = new FindRideDto();

        // Act & Assert
        // Testing with maximum valid latitude values
        assertDoesNotThrow(() -> {
            dto.setStartLatitude(90.0);
            dto.setEndLatitude(-90.0);
        });

        // Testing with maximum valid longitude values
        assertDoesNotThrow(() -> {
            dto.setStartLongitude(180.0);
            dto.setEndLongitude(-180.0);
        });

        assertEquals(90.0, dto.getStartLatitude());
        assertEquals(-90.0, dto.getEndLatitude());
        assertEquals(180.0, dto.getStartLongitude());
        assertEquals(-180.0, dto.getEndLongitude());
    }

    @Test
    void testDateTimeManipulation() {
        // Arrange
        FindRideDto dto = new FindRideDto();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        // Act
        dto.setDate(date);
        dto.setEndTime(time);

        // Assert
        assertEquals(date, dto.getDate());
        assertEquals(time, dto.getEndTime());

        // Test date manipulation
        LocalDate nextDay = date.plusDays(1);
        dto.setDate(nextDay);
        assertEquals(nextDay, dto.getDate());

        // Test time manipulation
        LocalTime laterTime = time.plusHours(2);
        dto.setEndTime(laterTime);
        assertEquals(laterTime, dto.getEndTime());
    }
}