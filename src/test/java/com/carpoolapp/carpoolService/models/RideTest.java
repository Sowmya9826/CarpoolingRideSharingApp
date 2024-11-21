package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.RideStatus;
import com.carpoolapp.carpoolService.models.enums.RideType;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.LocalTime;

public class RideTest {

    @Test
    public void testRideEntity() {
        // Create a Location for Pickup and Destination
        Location pickupLocation = new Location();
        pickupLocation.setAddress("123 Main St, Cityville");
        pickupLocation.setLatitude(40.7128);
        pickupLocation.setLongitude(-74.0060);

        Location destinationLocation = new Location();
        destinationLocation.setAddress("456 Elm St, Cityville");
        destinationLocation.setLatitude(40.7306);
        destinationLocation.setLongitude(-73.9352);

        // Create a Vehicle (without setting make, model, or year)
        Vehicle vehicle = new Vehicle();

        // Create Ride
        Ride ride = new Ride();
        ride.setVehicle(vehicle);
        ride.setPickupLocation(pickupLocation);
        ride.setDestinationLocation(destinationLocation);

        ride.setStartTime(LocalTime.of(9, 30));
        ride.setEndTime(LocalTime.of(10, 30));
        ride.setDate(LocalDate.of(2024, 11, 21));
        ride.setDaysOfWeek("Mon, Wed, Fri");
        ride.setAvailableSeats(3);
        ride.setCreatedDate(LocalDate.of(2024, 11, 20));

        // Assertions
        assertThat(ride).isNotNull();
        assertThat(ride.getVehicle()).isEqualTo(vehicle);
        assertThat(ride.getPickupLocation()).isEqualTo(pickupLocation);
        assertThat(ride.getDestinationLocation()).isEqualTo(destinationLocation);

        assertThat(ride.getStartTime()).isEqualTo(LocalTime.of(9, 30));
        assertThat(ride.getEndTime()).isEqualTo(LocalTime.of(10, 30));
        assertThat(ride.getDate()).isEqualTo(LocalDate.of(2024, 11, 21));
        assertThat(ride.getDaysOfWeek()).isEqualTo("Mon, Wed, Fri");
        assertThat(ride.getAvailableSeats()).isEqualTo(3);
        assertThat(ride.getCreatedDate()).isEqualTo(LocalDate.of(2024, 11, 20));
    }

    @Test
    public void testRideStatusEnum() {
        // Test if the RideStatus enum works correctly
        //assertThat(RideStatus.PENDING).isEqualTo(RideStatus.valueOf("PENDING"));
        assertThat(RideStatus.COMPLETED).isEqualTo(RideStatus.valueOf("COMPLETED"));
    }

}
