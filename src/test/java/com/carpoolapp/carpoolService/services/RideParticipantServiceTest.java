package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.RideParticipant;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import com.carpoolapp.carpoolService.repository.RideParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideParticipantServiceTest {

    @Mock
    private RideParticipantRepository rideParticipantRepository;

    @InjectMocks
    private RideParticipantService rideParticipantService;

    private Ride ride;
    private User user;

    @BeforeEach
    void setUp() {
        ride = new Ride();
        ride.setId(1L);

        user = new User();
        user.setId(1L);
    }

    @Test
    void createRideParticipantAsDriver_ShouldCreateParticipantWithDriverRole() {
        // Arrange
        ArgumentCaptor<RideParticipant> participantCaptor = ArgumentCaptor.forClass(RideParticipant.class);

        // Act
        rideParticipantService.createRideParticipantAsDriver(ride, user);

        // Assert
        verify(rideParticipantRepository).save(participantCaptor.capture());

        RideParticipant capturedParticipant = participantCaptor.getValue();
        assertEquals(ride, capturedParticipant.getRide());
        assertEquals(user, capturedParticipant.getParticipant());
        assertEquals(RideParticipantStatus.ACTIVE, capturedParticipant.getStatus());
        assertEquals(RideParticipateRole.DRIVER, capturedParticipant.getRole());
        assertEquals(LocalDate.now(), capturedParticipant.getJoinedAt());
    }

    @Test
    void createRideParticipantAsPassenger_ShouldCreateParticipantWithPassengerRole() {
        // Arrange
        ArgumentCaptor<RideParticipant> participantCaptor = ArgumentCaptor.forClass(RideParticipant.class);

        // Act
        rideParticipantService.createRideParticipantAsPassenger(ride, user);

        // Assert
        verify(rideParticipantRepository).save(participantCaptor.capture());

        RideParticipant capturedParticipant = participantCaptor.getValue();
        assertEquals(ride, capturedParticipant.getRide());
        assertEquals(user, capturedParticipant.getParticipant());
        assertEquals(RideParticipantStatus.ACTIVE, capturedParticipant.getStatus());
        assertEquals(RideParticipateRole.PASSENGER, capturedParticipant.getRole());
        assertEquals(LocalDate.now(), capturedParticipant.getJoinedAt());
    }

    @Test
    void markRideParticipantsAsCancelled_ShouldUpdateAllParticipantsStatus() {
        // Arrange
        RideParticipant participant1 = new RideParticipant();
        RideParticipant participant2 = new RideParticipant();
        when(rideParticipantRepository.findByRideId(ride.getId()))
                .thenReturn(Arrays.asList(participant1, participant2));

        // Act
        rideParticipantService.markRideParticipantsAsCancelled(ride);

        // Assert
        verify(rideParticipantRepository, times(2)).save(any(RideParticipant.class));
        assertEquals(RideParticipantStatus.CANCELLED, participant1.getStatus());
        assertEquals(RideParticipantStatus.CANCELLED, participant2.getStatus());
    }

    @Test
    void markRideParticipantAsCancelled_ShouldUpdateSpecificParticipantStatus() {
        // Arrange
        RideParticipant participant = new RideParticipant();
        participant.setStatus(RideParticipantStatus.ACTIVE);
        when(rideParticipantRepository.findByRideIdAndParticipantId(ride.getId(), user.getId()))
                .thenReturn(Optional.of(participant));

        // Act
        rideParticipantService.markRideParticipantAsCancelled(ride, user.getId());

        // Assert
        assertEquals(RideParticipantStatus.CANCELLED, participant.getStatus());
    }

    @Test
    void markRideParticipantAsCancelled_ShouldThrowException_WhenParticipantNotFound() {
        // Arrange
        when(rideParticipantRepository.findByRideIdAndParticipantId(ride.getId(), user.getId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                rideParticipantService.markRideParticipantAsCancelled(ride, user.getId()));
    }
}