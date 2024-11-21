package com.carpoolapp.carpoolService.respository;

import com.carpoolapp.carpoolService.dto.RideParticipantDto;
import com.carpoolapp.carpoolService.dto.UserRideInfoDto;
import com.carpoolapp.carpoolService.models.RideParticipant;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RideParticipantRepository extends JpaRepository<RideParticipant, Long> {
    List<RideParticipant> findByRideId(Long rideId);

    @Query("SELECT new com.carpoolapp.carpoolService.dto.UserRideInfoDto(rp.ride.id, rp.ride.date, rp.ride.daysOfWeek, rp.ride.startTime, rp.ride.endTime, rp.ride.pickupLocation, " +
            "rp.ride.destinationLocation, rp.role) " +
            "FROM RideParticipant rp " +
            "WHERE rp.participant.id = :userId " +
            "AND rp.ride.status = com.carpoolapp.carpoolService.models.enums.RideStatus.CREATED " +
            "AND rp.ride.type = com.carpoolapp.carpoolService.models.enums.RideType.ONE_TIME " +
            "AND (rp.ride.date > :currentDate " +
            "OR (rp.ride.date = :currentDate AND rp.ride.startTime > :currentTime)) " +
            "AND rp.status = :status")
    List<UserRideInfoDto> findUpcomingRidesForUser(@Param("userId") Long userId,
                                                   @Param("currentDate") LocalDate currentDate,
                                                   @Param("currentTime") LocalTime currentTime,
                                                   @Param("status") RideParticipantStatus status);

    @Query("SELECT new com.carpoolapp.carpoolService.dto.UserRideInfoDto(rp.ride.id, rp.ride.date, rp.ride.daysOfWeek, rp.ride.startTime, rp.ride.endTime, rp.ride.pickupLocation, " +
            "rp.ride.destinationLocation, rp.role) " +
            "FROM RideParticipant rp " +
            "WHERE rp.participant.id = :userId " +
            "AND rp.ride.status = com.carpoolapp.carpoolService.models.enums.RideStatus.CREATED " +
            "AND rp.ride.type = com.carpoolapp.carpoolService.models.enums.RideType.RECURRING " +
            "AND rp.status = :status")
    List<UserRideInfoDto> findRecurringRidesForUser(@Param("userId") Long userId,
                                                    @Param("status") RideParticipantStatus status);


    @Query("SELECT new com.carpoolapp.carpoolService.dto.UserRideInfoDto(rp.ride.id, rp.ride.date, rp.ride.daysOfWeek, rp.ride.startTime, rp.ride.endTime, rp.ride.pickupLocation, " +
            "rp.ride.destinationLocation, rp.role) " +
            "FROM RideParticipant rp " +
            "WHERE rp.participant.id = :userId " +
            "AND (rp.ride.date < :currentDate " +
            "OR (rp.ride.date = :currentDate AND rp.ride.endTime < :currentTime)) " +
            "AND rp.status = :status")
    List<UserRideInfoDto> findPastRidesForUser(@Param("userId") Long userId,
                                               @Param("currentDate") LocalDate currentDate,
                                               @Param("currentTime") LocalTime currentTime,
                                               @Param("status") RideParticipantStatus status);


    @Query("SELECT new com.carpoolapp.carpoolService.dto.RideParticipantDto(rp.ride.id, rp.participant.firstName, rp.participant.lastName, " +
            "rp.participant.phoneNumber, rp.participant.emailId, rp.participant.profileImage, rp.role) " +
            "FROM RideParticipant rp " +
            "WHERE rp.ride.id = :rideId " +
            "AND rp.status = com.carpoolapp.carpoolService.models.enums.RideParticipantStatus.ACTIVE")
    List<RideParticipantDto> getActiveRidePassengersDetails(Long rideId);

    Optional<RideParticipant> findByRideIdAndParticipantId(Long id, Long userId);

    @Query("SELECT COUNT(rp) FROM RideParticipant rp WHERE rp.ride.id = :rideId " +
            "AND rp.status = com.carpoolapp.carpoolService.models.enums.RideParticipantStatus.ACTIVE " +
            "AND rp.role = com.carpoolapp.carpoolService.models.enums.RideParticipateRole.PASSENGER")
    int countActivePassengersInRide(Long rideId);
}
