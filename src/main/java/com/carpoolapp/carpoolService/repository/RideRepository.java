package com.carpoolapp.carpoolService.repository;

import com.carpoolapp.carpoolService.dto.MatchingRideDto;
import com.carpoolapp.carpoolService.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long>  {

    @Query("SELECT new com.carpoolapp.carpoolService.dto.MatchingRideDto(r.id, r.date, r.daysOfWeek, r.startTime, r.endTime, r.status, r.availableSeats, " +
            "r.pickupLocation.address, r.pickupLocation.latitude, r.pickupLocation.longitude, " +
            "r.destinationLocation.address, r.destinationLocation.latitude, r.destinationLocation.longitude) " +
            "FROM Ride r " +
            "WHERE r.date = :date AND " +
            "r.endTime BETWEEN :endTimeMinus AND :endTimePlus AND " +
            "r.status = com.carpoolapp.carpoolService.models.enums.RideStatus.CREATED AND " +
            "r.type = com.carpoolapp.carpoolService.models.enums.RideType.ONE_TIME AND " +
            "r.availableSeats >= 1 AND " +
            "NOT EXISTS (" +
            "  SELECT rp.id FROM RideParticipant rp " +
            "  WHERE rp.ride.id = r.id AND rp.participant.id = :userId" +
            ")")
    List<MatchingRideDto> findOneTimeRidesWithLocationsExcludingUser(@Param("date") LocalDate date,
                                                                     @Param("endTimeMinus") LocalTime endTimeMinus,
                                                                     @Param("endTimePlus") LocalTime endTimePlus,
                                                                     @Param("userId") Long userId);


    @Query("SELECT new com.carpoolapp.carpoolService.dto.MatchingRideDto(r.id, r.date, r.daysOfWeek, r.startTime, r.endTime, r.status, r.availableSeats, " +
            "r.pickupLocation.address, r.pickupLocation.latitude, r.pickupLocation.longitude, " +
            "r.destinationLocation.address, r.destinationLocation.latitude, r.destinationLocation.longitude) " +
            "FROM Ride r " +
            "WHERE r.endTime BETWEEN :endTimeMinus AND :endTimePlus AND " +
            "r.status = com.carpoolapp.carpoolService.models.enums.RideStatus.CREATED AND " +
            "r.type = com.carpoolapp.carpoolService.models.enums.RideType.RECURRING AND " +
            "r.availableSeats >= 1 AND " +
            "NOT EXISTS (" +
            "  SELECT rp.id FROM RideParticipant rp " +
            "  WHERE rp.ride.id = r.id AND rp.participant.id = :userId" +
            ")")
    List<MatchingRideDto> findRecurringRidesWithLocationsExcludingUser(@Param("endTimeMinus") LocalTime endTimeMinus,
                                                                       @Param("endTimePlus") LocalTime endTimePlus,
                                                                       @Param("userId") Long userId);
}




