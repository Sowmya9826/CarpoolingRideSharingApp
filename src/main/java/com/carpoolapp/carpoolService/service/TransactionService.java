package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.dto.RideOwedDto;
import com.carpoolapp.carpoolService.models.Fare;
import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.Transaction;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.enums.TransactionStatus;
import com.carpoolapp.carpoolService.models.enums.TransactionType;
import com.carpoolapp.carpoolService.respository.FareRepository;
import com.carpoolapp.carpoolService.respository.RideParticipantRepository;
import com.carpoolapp.carpoolService.respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FareService fareService;

    @Autowired
    private RideParticipantRepository rideParticipantRepository;

    public void updateTransactionAmountOfPassengers(Ride ride) {
        // count the number of passengers in the ride
        int passengerCount = rideParticipantRepository.countActivePassengersInRide(ride.getId());

        // calculate per passenger fare
        double perPassengerFare = fareService.calculatePerPassengerFare(ride, passengerCount);

        Fare fare = fareRepository.findByRideId(ride.getId())
                .orElseThrow(() -> new RuntimeException("Fare not found"));

        transactionRepository.getTransactionsOfAFare(fare.getId())
                .forEach(transaction -> {
                    transaction.setAmount(perPassengerFare);
                    transactionRepository.save(transaction);
                });
    }

    public void createTransactionForPassenger(Ride ride, User passenger, TransactionType type, String desc) {
        Fare fare = fareRepository.findByRideId(ride.getId())
                .orElseThrow(() -> new RuntimeException("Fare not found for the ride"));

        Transaction transaction = new Transaction();
        transaction.setFare(fare);
        transaction.setUser(passenger);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setType(type);
        transaction.setDescription(desc);
        transactionRepository.save(transaction);
    }

    public void deleteTransactionsForRide(Ride ride) {
        Fare fare = fareRepository.findByRideId(ride.getId())
                .orElseThrow(() -> new RuntimeException("Fare not found for the ride"));

        transactionRepository.getTransactionsOfAFare(fare.getId())
                .forEach(transactionRepository::delete);
    }

    public void deleteTransactionForUserInRide(Long userId, Ride ride) {
        Fare fare = fareRepository.findByRideId(ride.getId())
                .orElseThrow(() -> new RuntimeException("Fare not found for the ride"));

        transactionRepository.getTransactionsOfAFare(fare.getId())
                .stream()
                .filter(transaction -> transaction.getUser().getId().equals(userId))
                .findFirst()
                .ifPresent(transactionRepository::delete);
    }

    public List<RideOwedDto> getRidesWhereUserOwes(Long userId) {
        List<Transaction> transactions = transactionRepository.findTransactionsOwedByUser(userId, LocalDateTime.now(ZoneId.of("UTC")).toLocalDate(),
                LocalDateTime.now(ZoneId.of("UTC")).toLocalTime());
        return transactions.stream().map(t -> {
            Ride ride = t.getFare().getRide();
            User driver = ride.getVehicle().getOwner();
            return new RideOwedDto(
                    ride.getId(),
                    ride.getPickupLocation().getAddress(),
                    ride.getDestinationLocation().getAddress(),
                    ride.getDate(),
                    driver.getFirstName(),
                    driver.getLastName(),
                    driver.getPhoneNumber(),
                    t.getAmount()
            );
        }).collect(Collectors.toList());
    }

    public void payAllOwedByUser(Long userId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Fetch all pending transactions for past rides
        List<Transaction> pendingTransactions = transactionRepository.findPendingTransactionsForPastRides(userId, currentDate, currentTime);

        if (pendingTransactions.isEmpty()) {
            return;
        }

        // Update each transaction's status to COMPLETED
        for (Transaction transaction : pendingTransactions) {
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setCompletedDate(LocalDate.now());
        }

        transactionRepository.saveAll(pendingTransactions);
    }

    public void payForRide(Long userId, Long rideId) {
        // Fetch pending transactions for the user and ride
        List<Transaction> pendingTransactions = transactionRepository.findPendingTransactionsByUserAndRide(userId, rideId);

        if (pendingTransactions.isEmpty()) {
            // No transactions to process
            return;
        }

        // Update transaction statuses to COMPLETED
        for (Transaction transaction : pendingTransactions) {
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setCompletedDate(LocalDate.now()); // Optional: Set the completion date
        }

        // Save updated transactions
        transactionRepository.saveAll(pendingTransactions);
    }
}
