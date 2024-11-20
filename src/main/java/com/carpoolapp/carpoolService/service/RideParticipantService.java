package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.RideParticipant;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import com.carpoolapp.carpoolService.respository.RideParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RideParticipantService {

    @Autowired
    private RideParticipantRepository rideParticipantRepository;

    public void createRideParticipantAsDriver(Ride ride, User user) {
        RideParticipant rideParticipant = new RideParticipant();
        rideParticipant.setRide(ride);
        rideParticipant.setParticipant(user);
        rideParticipant.setStatus(RideParticipantStatus.ACTIVE);
        rideParticipant.setRole(RideParticipateRole.DRIVER);
        rideParticipant.setJoinedAt(LocalDate.now());

        rideParticipantRepository.save(rideParticipant);
    }

    public void createRideParticipantAsPassenger(Ride ride, User user) {
        RideParticipant rideParticipant = new RideParticipant();
        rideParticipant.setRide(ride);
        rideParticipant.setParticipant(user);
        rideParticipant.setStatus(RideParticipantStatus.ACTIVE);
        rideParticipant.setRole(RideParticipateRole.PASSENGER);
        rideParticipant.setJoinedAt(LocalDate.now());

        rideParticipantRepository.save(rideParticipant);
    }

    public void markRideParticipantsAsCancelled(Ride ride) {
        rideParticipantRepository.findByRideId(ride.getId())
                .forEach(rideParticipant -> {
                    rideParticipant.setStatus(RideParticipantStatus.CANCELLED);
                    rideParticipantRepository.save(rideParticipant); // Save explicitly
                });
    }

    public void markRideParticipantAsCancelled(Ride ride, Long userId) {
        // find the ride participant where ride id and user id matches
        RideParticipant rideParticipant = rideParticipantRepository.findByRideIdAndParticipantId(ride.getId(), userId)
                .orElseThrow(() -> new RuntimeException("Ride participant not found"));

        rideParticipant.setStatus(RideParticipantStatus.CANCELLED);
    }
}
