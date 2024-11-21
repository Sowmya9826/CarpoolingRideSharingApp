//package com.carpoolapp.carpoolService.repository;
//
//import com.carpoolapp.carpoolService.models.Fare;
//import com.carpoolapp.carpoolService.models.Ride;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class FareRepositoryTest {
//
//    @Mock
//    private FareRepository fareRepository;
//
//    @InjectMocks
//    //private FareService fareService; // If you have a service layer that uses FareRepository
//
//    private Fare fare;
//    private Ride ride;
//
//    @BeforeEach
//    public void setUp() {
//        // Create a Ride and Fare instance
//        ride = new Ride();
//        ride.setId(1L); // Set the ride ID to 1 for testing
//
//        fare = new Fare();
//        fare.setId(1L);
//        fare.setRide(ride); // Associate the fare with the ride
//        fare.setAmount(50.0);
//    }
//
//    @Test
//    public void testFindByRideId_Success() {
//        // Arrange: Mock the repository method to return an Optional containing the fare
//        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
//
//        // Act: Call the method on the repository
//        Optional<Fare> result = fareRepository.findByRideId(ride.getId());
//
//        // Assert: Verify that the returned Fare is correct
//        assertThat(result).isPresent();
//        assertThat(result.get().getId()).isEqualTo(1L);
//        assertThat(result.get().getRide()).isEqualTo(ride);
//        assertThat(result.get().getAmount()).isEqualTo(50.0);
//
//        // Verify that the repository method was called once with the correct argument
//        verify(fareRepository, times(1)).findByRideId(ride.getId());
//    }
//
//    @Test
//    public void testFindByRideId_NotFound() {
//        // Arrange: Mock the repository method to return an empty Optional
//        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());
//
//        // Act: Call the method on the repository
//        Optional<Fare> result = fareRepository.findByRideId(ride.getId());
//
//        // Assert: Verify that the returned Optional is empty
//        assertThat(result).isEmpty();
//
//        // Verify that the repository method was called once with the correct argument
//        verify(fareRepository, times(1)).findByRideId(ride.getId());
//    }
//}
