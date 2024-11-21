package com.carpoolapp.carpoolService.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareTest {

    private Fare fare;
    private Ride ride;  // This assumes you have a Ride object to associate with the Fare

    private final Long rideId = 1L;
    private final double amount = 25.50;

    @BeforeEach
    void setUp() {
        // Setting up the Ride object (it could be mocked if necessary)
        ride = new Ride();
        ride.setId(rideId);

        // Create the Fare object
        fare = new Fare();
        fare.setRide(ride);
        fare.setAmount(amount);
    }

    @Test
    void testConstructorAndGetters() {
        // Verifying that the constructor and getters work as expected
        assertNotNull(fare);
        assertEquals(ride, fare.getRide());
        assertEquals(amount, fare.getAmount());
    }

    @Test
    void testSetters() {
        // Verifying that setters update the values correctly
        Ride newRide = new Ride();
        newRide.setId(2L);

        fare.setRide(newRide);
        fare.setAmount(30.75);

        assertEquals(newRide, fare.getRide());
        assertEquals(30.75, fare.getAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create another Fare object with the same values
        Fare fare2 = new Fare();
        fare2.setRide(ride);
        fare2.setAmount(amount);

        // Test equality and hashcode
        assertEquals(fare, fare2);
        assertEquals(fare.hashCode(), fare2.hashCode());

        // Modify fare2 and test inequality
        fare2.setAmount(35.00);
        assertNotEquals(fare, fare2);
    }

//    @Test
//    void testToString() {
//        // Test that toString generates the correct string representation
//        String expectedString = "Fare(id=null, ride=Ride(id=1), amount=25.5)";
//        // This assumes that the Ride object has a proper toString implementation that displays its id
//        assertTrue(fare.toString().contains("Fare(id=null, ride=Ride(id=1), amount=25.5)"));
//    }

    @Test
    void testFareWithNullRide() {
        // Test Fare with null Ride
        Fare fareWithNullRide = new Fare();
        fareWithNullRide.setAmount(50.00);

        // No Ride object set
        assertNull(fareWithNullRide.getRide());
        assertEquals(50.00, fareWithNullRide.getAmount());
    }
}
