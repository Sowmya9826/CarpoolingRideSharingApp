package com.carpoolapp.carpoolService.service;

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
}
