package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.RideParticipant;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import com.carpoolapp.carpoolService.respository.RideParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        rideParticipantRepository.save(rideParticipant);
    }

    public void createRideParticipantAsPassenger(Ride ride, User user) {
        RideParticipant rideParticipant = new RideParticipant();
        rideParticipant.setRide(ride);
        rideParticipant.setParticipant(user);
        rideParticipant.setStatus(RideParticipantStatus.ACTIVE);
        rideParticipant.setRole(RideParticipateRole.PASSENGER);

        rideParticipantRepository.save(rideParticipant);
    }
}
