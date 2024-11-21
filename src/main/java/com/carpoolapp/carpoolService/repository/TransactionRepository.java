package com.carpoolapp.carpoolService.repository;

import com.carpoolapp.carpoolService.dto.RideOwedToUserDto;
import com.carpoolapp.carpoolService.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.fare.id = :fareId")
    List<Transaction> getTransactionsOfAFare(@Param("fareId") Long fareId);

    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.status = com.carpoolapp.carpoolService.models.enums.TransactionStatus.PENDING " +
            "AND (t.fare.ride.date < :currentDate " +
            "OR (t.fare.ride.date = :currentDate AND t.fare.ride.endTime < :currentTime))")
    Double findTotalOwedByUser(@Param("userId") Long userId,
                               @Param("currentDate") LocalDate currentDate,
                               @Param("currentTime") LocalTime currentTime);

    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "JOIN t.fare.ride r " +
            "JOIN RideParticipant rp ON rp.ride.id = r.id " +
            "WHERE rp.participant.id = :userId " +
            "AND rp.role = com.carpoolapp.carpoolService.models.enums.RideParticipateRole.DRIVER " +
            "AND (r.date < :currentDate " +
            "OR (r.date = :currentDate AND r.endTime < :currentTime)) " +
            "AND t.status = com.carpoolapp.carpoolService.models.enums.TransactionStatus.PENDING")
    Double findTotalOwedToUser(@Param("userId") Long userId,
                               @Param("currentDate") LocalDate currentDate,
                               @Param("currentTime") LocalTime currentTime);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.status = com.carpoolapp.carpoolService.models.enums.TransactionStatus.PENDING " +
            "AND (t.fare.ride.date < :currentDate " +
            "OR (t.fare.ride.date = :currentDate AND t.fare.ride.endTime < :currentTime))")
    List<Transaction> findTransactionsOwedByUser(@Param("userId") Long userId,
                                                 @Param("currentDate") LocalDate currentDate,
                                                 @Param("currentTime") LocalTime currentTime);

    @Query("SELECT new com.carpoolapp.carpoolService.dto.RideOwedToUserDto(r.id, r.pickupLocation.address, r.destinationLocation.address, " +
            "r.date, SUM(t.amount)) " +
            "FROM Transaction t " +
            "JOIN t.fare.ride r " +
            "JOIN RideParticipant rp ON rp.ride.id = r.id " +
            "WHERE rp.participant.id = :userId " +
            "AND rp.role = com.carpoolapp.carpoolService.models.enums.RideParticipateRole.DRIVER " +
            "AND (r.date < :currentDate " +
            "OR (r.date = :currentDate AND r.endTime < :currentTime)) " +
            "AND t.status = com.carpoolapp.carpoolService.models.enums.TransactionStatus.PENDING " +
            "GROUP BY r.id, r.pickupLocation.address, r.destinationLocation.address, r.startTime, r.endTime, r.date")
    List<RideOwedToUserDto> findRidesOwedToUser(@Param("userId") Long userId,
                                                @Param("currentDate") LocalDate currentDate,
                                                @Param("currentTime") LocalTime currentTime);

    @Query("SELECT t FROM Transaction t " +
            "JOIN t.fare.ride r " +
            "WHERE t.user.id = :userId " +
            "AND t.status = com.carpoolapp.carpoolService.models.enums.TransactionStatus.PENDING " +
            "AND (r.date < :currentDate " +
            "OR (r.date = :currentDate AND r.endTime < :currentTime))")
    List<Transaction> findPendingTransactionsForPastRides(@Param("userId") Long userId,
                                                          @Param("currentDate") LocalDate currentDate,
                                                          @Param("currentTime") LocalTime currentTime);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.user.id = :userId " +
            "AND t.fare.ride.id = :rideId " +
            "AND t.status = com.carpoolapp.carpoolService.models.enums.TransactionStatus.PENDING")
    List<Transaction> findPendingTransactionsByUserAndRide(@Param("userId") Long userId,
                                                           @Param("rideId") Long rideId);
}
