package com.carpoolapp.carpoolService.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LocationTest {

    @Test
    public void testLocationEntity() {
        // Create a Location object
        Location location = new Location();
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);
        location.setAddress("New York, NY");

        // Assertions to verify the correct values are set
        assertThat(location).isNotNull();
        assertThat(location.getLatitude()).isEqualTo(40.7128);
        assertThat(location.getLongitude()).isEqualTo(-74.0060);
        assertThat(location.getAddress()).isEqualTo("New York, NY");
    }
}
