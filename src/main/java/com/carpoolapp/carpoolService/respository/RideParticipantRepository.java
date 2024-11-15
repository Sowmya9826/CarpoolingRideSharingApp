package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.models.RideParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideParticipantRepository extends JpaRepository<RideParticipant, String> {
}
