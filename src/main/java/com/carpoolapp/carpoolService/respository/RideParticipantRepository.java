package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.dto.UserRideInfoDto;
import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.RideParticipant;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RideParticipantRepository extends JpaRepository<RideParticipant, Long> {
    List<RideParticipant> findByRideId(Long rideId);

    @Query("SELECT new com.carpoolapp.carpoolService.dto.UserRideInfoDto(rp.ride.id, rp.ride.date, rp.ride.startTime, rp.ride.pickupLocation, " +
            "rp.ride.destinationLocation, rp.role) " +
            "FROM RideParticipant rp " +
            "WHERE rp.participant.id = :userId " +
            "AND rp.ride.status = com.carpoolapp.carpoolService.models.enums.RideStatus.CREATED " +
            "AND (rp.ride.date > :currentDate " +
            "OR (rp.ride.date = :currentDate AND rp.ride.startTime > :currentTime)) " +
            "AND rp.status = :status")
    List<UserRideInfoDto> findUpcomingRidesForUser(@Param("userId") Long userId,
                                                   @Param("currentDate") LocalDate currentDate,
                                                   @Param("currentTime") LocalTime currentTime,
                                                   @Param("status") RideParticipantStatus status);
}
