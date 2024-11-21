package com.carpoolapp.carpoolService.models;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VehicleTest {

    @Test
    public void testVehicleEntity() {
        // Create a User (Owner of the vehicle)
        User owner = new User();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setEmailId("johndoe@example.com");
        owner.setPhoneNumber("+1234567890");
        owner.setPassword("password123");

        // Create a Vehicle instance
        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setNumber("ABC1234");
        vehicle.setType("Sedan");
        vehicle.setName("Toyota Camry");
        vehicle.setColor("Blue");
        vehicle.setSeatCount(5);

        // Assertions
        assertThat(vehicle).isNotNull();
        assertThat(vehicle.getOwner()).isEqualTo(owner); // Check if owner is correctly set
        assertThat(vehicle.getNumber()).isEqualTo("ABC1234");
        assertThat(vehicle.getType()).isEqualTo("Sedan");
        assertThat(vehicle.getName()).isEqualTo("Toyota Camry");
        assertThat(vehicle.getColor()).isEqualTo("Blue");
        assertThat(vehicle.getSeatCount()).isEqualTo(5);
    }

    @Test
    public void testVehicleWithoutOwner() {
        // Create a Vehicle without setting owner
        Vehicle vehicle = new Vehicle();
        vehicle.setNumber("XYZ9876");
        vehicle.setType("SUV");
        vehicle.setName("Honda CR-V");
        vehicle.setColor("Black");
        vehicle.setSeatCount(7);

        // Assertions
        assertThat(vehicle).isNotNull();
        assertThat(vehicle.getOwner()).isNull(); // Ensure that owner is null if not set
        assertThat(vehicle.getNumber()).isEqualTo("XYZ9876");
        assertThat(vehicle.getType()).isEqualTo("SUV");
        assertThat(vehicle.getName()).isEqualTo("Honda CR-V");
        assertThat(vehicle.getColor()).isEqualTo("Black");
        assertThat(vehicle.getSeatCount()).isEqualTo(7);
    }
}
