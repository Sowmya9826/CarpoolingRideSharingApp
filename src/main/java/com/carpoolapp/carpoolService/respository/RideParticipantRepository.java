package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.RideParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideParticipantRepository extends JpaRepository<RideParticipant, Long> {
    List<RideParticipant> findByRideId(Long rideId);
}
