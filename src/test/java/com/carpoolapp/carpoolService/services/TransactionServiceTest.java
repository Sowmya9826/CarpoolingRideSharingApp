package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.models.*;
import com.carpoolapp.carpoolService.models.enums.TransactionStatus;
import com.carpoolapp.carpoolService.models.enums.TransactionType;
import com.carpoolapp.carpoolService.repository.FareRepository;
import com.carpoolapp.carpoolService.repository.RideParticipantRepository;
import com.carpoolapp.carpoolService.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private FareRepository fareRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private FareService fareService;

    @Mock
    private RideParticipantRepository rideParticipantRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Ride ride;
    private User passenger;
    private Fare fare;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
        ride = new Ride();
        ride.setId(1L);

        passenger = new User();
        passenger.setId(1L);

        fare = new Fare();
        fare.setId(1L);
        fare.setRide(ride);

        transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setFare(fare);
        transaction1.setUser(passenger);
        transaction1.setAmount(100.0);

        transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setFare(fare);
        transaction2.setUser(passenger);
        transaction2.setAmount(100.0);
    }

    @Test
    void updateTransactionAmountOfPassengers_Success() {
        // Arrange
        when(rideParticipantRepository.countActivePassengersInRide(ride.getId())).thenReturn(2);
        when(fareService.calculatePerPassengerFare(ride, 2)).thenReturn(50.0);
        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
        when(transactionRepository.getTransactionsOfAFare(fare.getId()))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        // Act
        transactionService.updateTransactionAmountOfPassengers(ride);

        // Assert
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        assertEquals(50.0, transaction1.getAmount());
        assertEquals(50.0, transaction2.getAmount());
    }

    @Test
    void updateTransactionAmountOfPassengers_FareNotFound() {
        // Arrange
        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> transactionService.updateTransactionAmountOfPassengers(ride));
    }

//    @Test
//    void createTransactionForPassenger_Success() {
//        // Arrange
//        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
//
//        // Act
//        transactionService.createTransactionForPassenger(
//                ride, passenger, TransactionType.PAYMENT, "Test payment");
//
//        // Assert
//        verify(transactionRepository).save(argThat(transaction ->
//                transaction.getFare().equals(fare) &&
//                        transaction.getUser().equals(passenger) &&
//                        transaction.getStatus() == TransactionStatus.PENDING &&
//                        transaction.getType() == TransactionType.PAYMENT &&
//                        transaction.getDescription().equals("Test payment")
//        ));
//    }
//
//    @Test
//    void createTransactionForPassenger_FareNotFound() {
//        // Arrange
//        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(RuntimeException.class,
//                () -> transactionService.createTransactionForPassenger(
//                        ride, passenger, TransactionType.PAYMENT, "Test payment"));
//    }

    @Test
    void deleteTransactionsForRide_Success() {
        // Arrange
        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
        when(transactionRepository.getTransactionsOfAFare(fare.getId()))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        // Act
        transactionService.deleteTransactionsForRide(ride);

        // Assert
        verify(transactionRepository, times(2)).delete(any(Transaction.class));
    }

    @Test
    void deleteTransactionsForRide_FareNotFound() {
        // Arrange
        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> transactionService.deleteTransactionsForRide(ride));
    }

    @Test
    void deleteTransactionForUserInRide_Success() {
        // Arrange
        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
        when(transactionRepository.getTransactionsOfAFare(fare.getId()))
                .thenReturn(Arrays.asList(transaction1));

        // Act
        transactionService.deleteTransactionForUserInRide(passenger.getId(), ride);

        // Assert
        verify(transactionRepository).delete(transaction1);
    }

    @Test
    void deleteTransactionForUserInRide_FareNotFound() {
        // Arrange
        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> transactionService.deleteTransactionForUserInRide(passenger.getId(), ride));
    }

    @Test
    void deleteTransactionForUserInRide_TransactionNotFound() {
        // Arrange
        when(fareRepository.findByRideId(ride.getId())).thenReturn(Optional.of(fare));
        when(transactionRepository.getTransactionsOfAFare(fare.getId()))
                .thenReturn(Arrays.asList());

        // Act
        transactionService.deleteTransactionForUserInRide(passenger.getId(), ride);

        // Assert
        verify(transactionRepository, never()).delete(any());
    }
}