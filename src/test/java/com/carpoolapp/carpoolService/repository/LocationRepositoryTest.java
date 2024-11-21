//package com.carpoolapp.carpoolService.repository;
//
//import com.carpoolapp.carpoolService.models.Location;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class LocationRepositoryTest {
//
//    @Autowired
//    private LocationRepository locationRepository;
//
//    private Location location;
//
//    @BeforeEach
//    public void setUp() {
//        // Create a Location object to be saved
//        location = new Location();
//        location.setLatitude(40.7128);
//        location.setLongitude(-74.0060);
//        location.setAddress("New York, NY");
//    }
//
//    @Test
//    public void testSaveLocation() {
//        // Save the location and verify that it was saved
//        Location savedLocation = locationRepository.save(location);
//
//        // Assert that the saved location is not null and the fields match
//        assertThat(savedLocation).isNotNull();
//        assertThat(savedLocation.getId()).isNotNull();  // ID should be assigned after save
//        assertThat(savedLocation.getLatitude()).isEqualTo(40.7128);
//        assertThat(savedLocation.getLongitude()).isEqualTo(-74.0060);
//        assertThat(savedLocation.getAddress()).isEqualTo("New York, NY");
//    }
//
//    @Test
//    public void testFindLocationById() {
//        // Save the location to the database
//        Location savedLocation = locationRepository.save(location);
//
//        // Find the location by its ID
//        Location foundLocation = locationRepository.findById(savedLocation.getId()).orElse(null);
//
//        // Assert that the location is found and its fields are correct
//        assertThat(foundLocation).isNotNull();
//        assertThat(foundLocation.getId()).isEqualTo(savedLocation.getId());
//        assertThat(foundLocation.getLatitude()).isEqualTo(savedLocation.getLatitude());
//        assertThat(foundLocation.getLongitude()).isEqualTo(savedLocation.getLongitude());
//        assertThat(foundLocation.getAddress()).isEqualTo(savedLocation.getAddress());
//    }
//
//    @Test
//    public void testDeleteLocation() {
//        // Save the location
//        Location savedLocation = locationRepository.save(location);
//
//        // Delete the location by its ID
//        locationRepository.deleteById(savedLocation.getId());
//
//        // Verify that the location no longer exists in the repository
//        Location deletedLocation = locationRepository.findById(savedLocation.getId()).orElse(null);
//        assertThat(deletedLocation).isNull();
//    }
//}
