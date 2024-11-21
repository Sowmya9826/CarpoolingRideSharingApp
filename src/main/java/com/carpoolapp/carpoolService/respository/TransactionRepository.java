package com.carpoolapp.carpoolService.respository;

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
}
