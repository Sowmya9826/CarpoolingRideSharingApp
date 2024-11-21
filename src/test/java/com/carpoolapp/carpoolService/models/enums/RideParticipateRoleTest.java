package com.carpoolapp.carpoolService.models.enums;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RideParticipateRoleTest {

    @Test
    public void testRideParticipateRoleEnumValues() {
        // Verify that the enum values are correctly set
        assertThat(RideParticipateRole.DRIVER).isNotNull();
        assertThat(RideParticipateRole.PASSENGER).isNotNull();
    }

    @Test
    public void testEnumNames() {
        // Verify that the name of the enum constants are correct
        assertThat(RideParticipateRole.DRIVER.name()).isEqualTo("DRIVER");
        assertThat(RideParticipateRole.PASSENGER.name()).isEqualTo("PASSENGER");
    }

    @Test
    public void testEnumValues() {
        // Verify that the enum constants are correctly used in a switch case or other logic
        assertThat(RideParticipateRole.valueOf("DRIVER")).isEqualTo(RideParticipateRole.DRIVER);
        assertThat(RideParticipateRole.valueOf("PASSENGER")).isEqualTo(RideParticipateRole.PASSENGER);
    }

    @Test
    public void testEnumOrdinal() {
        // Verify that the ordinals of the enum constants are correct
        assertThat(RideParticipateRole.DRIVER.ordinal()).isEqualTo(0);
        assertThat(RideParticipateRole.PASSENGER.ordinal()).isEqualTo(1);
    }
}
