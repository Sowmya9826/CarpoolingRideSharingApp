package com.carpoolapp.carpoolService.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDtoTest {

    private VehicleDto vehicleDto;

    private final Long userId = 1L;
    private final String number = "ABC123";
    private final String type = "Sedan";
    private final String name = "Honda Civic";
    private final String color = "Blue";
    private final int seatCount = 4;

    @BeforeEach
    void setUp() {
        vehicleDto = new VehicleDto();
        vehicleDto.setUserId(userId);
        vehicleDto.setNumber(number);
        vehicleDto.setType(type);
        vehicleDto.setName(name);
        vehicleDto.setColor(color);
        vehicleDto.setSeatCount(seatCount);
    }

    @Test
    void testConstructorAndGetters() {
        // Verifying that the constructor initializes fields correctly via getters
        assertNotNull(vehicleDto);
        assertEquals(userId, vehicleDto.getUserId());
        assertEquals(number, vehicleDto.getNumber());
        assertEquals(type, vehicleDto.getType());
        assertEquals(name, vehicleDto.getName());
        assertEquals(color, vehicleDto.getColor());
        assertEquals(seatCount, vehicleDto.getSeatCount());
    }

    @Test
    void testSetters() {
        // Verifying that setters update values correctly
        vehicleDto.setUserId(2L);
        vehicleDto.setNumber("XYZ987");
        vehicleDto.setType("SUV");
        vehicleDto.setName("Toyota RAV4");
        vehicleDto.setColor("Red");
        vehicleDto.setSeatCount(5);

        assertEquals(2L, vehicleDto.getUserId());
        assertEquals("XYZ987", vehicleDto.getNumber());
        assertEquals("SUV", vehicleDto.getType());
        assertEquals("Toyota RAV4", vehicleDto.getName());
        assertEquals("Red", vehicleDto.getColor());
        assertEquals(5, vehicleDto.getSeatCount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Creating another VehicleDto object with the same values
        VehicleDto vehicleDto2 = new VehicleDto();
        vehicleDto2.setUserId(userId);
        vehicleDto2.setNumber(number);
        vehicleDto2.setType(type);
        vehicleDto2.setName(name);
        vehicleDto2.setColor(color);
        vehicleDto2.setSeatCount(seatCount);

        // Testing equality and hashcode
        assertEquals(vehicleDto, vehicleDto2);
        assertEquals(vehicleDto.hashCode(), vehicleDto2.hashCode());

        // Modify vehicleDto2 and test inequality
        vehicleDto2.setSeatCount(6);
        assertNotEquals(vehicleDto, vehicleDto2);
    }

    @Test
    void testToString() {
        // Test that toString contains relevant fields
        String expectedString = "VehicleDto(userId=" + userId + ", number=" + number + ", type=" + type +
                ", name=" + name + ", color=" + color + ", seatCount=" + seatCount + ")";
        assertEquals(expectedString, vehicleDto.toString());
    }
}
