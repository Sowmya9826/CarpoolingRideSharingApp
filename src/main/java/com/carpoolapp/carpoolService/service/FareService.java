package com.carpoolapp.carpoolService.service;


import com.carpoolapp.carpoolService.models.Fare;
import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.repository.FareRepository;
import com.carpoolapp.carpoolService.repository.RideParticipantRepository;
import com.carpoolapp.carpoolService.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FareService {

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideParticipantRepository rideParticipantRepository;

    private static final double FARE_PER_KM = 1.0;  // Set fare per kilometer (or any other currency unit)

    public void createFare(Ride ride) {
        Fare fare = new Fare();
        double amount = calculateFare(ride.getPickupLocation().getLatitude(), ride.getPickupLocation().getLongitude(),
                ride.getDestinationLocation().getLatitude(), ride.getDestinationLocation().getLongitude());
        fare.setAmount(amount);
        fare.setRide(ride);
        fareRepository.save(fare);
    }

    public double calculatePerPassengerFare(Ride ride, int passengerCount) {
        Optional<Fare> fareOptional = fareRepository.findByRideId(ride.getId());
        if (fareOptional.isEmpty()) {
            throw new RuntimeException("Fare not found for the ride");
        }

        double totalFare = fareOptional.get().getAmount();

        return (passengerCount > 0) ? totalFare / passengerCount : totalFare;
    }

    /**
     * Calculates the fare between the start and destination coordinates using the Haversine formula.
     * @param startLat  Latitude of the starting point
     * @param startLng  Longitude of the starting point
     * @param endLat    Latitude of the destination
     * @param endLng    Longitude of the destination
     * @return          Estimated fare in the currency unit
     */
    public double calculateFare(double startLat, double startLng, double endLat, double endLng) {
        double distance = calculateDistance(startLat, startLng, endLat, endLng);
        return distance * FARE_PER_KM;  // Multiply by the rate per km
    }

    public double getEstimatedFare(Long rideId) {
        Fare fare = fareRepository.findByRideId(rideId)
                .orElseThrow(() -> new RuntimeException("Fare not found for the ride"));

        int passengerCount = rideParticipantRepository.countActivePassengersInRide(rideId) + 1;  // Add the passenger looking for a ride

        return (fare.getAmount() / passengerCount);
    }

    public void deleteFareForRide(Ride ride) {
        Fare fare = fareRepository.findByRideId(ride.getId())
                .orElseThrow(() -> new RuntimeException("Fare not found for the ride"));
        fareRepository.delete(fare);
    }

    /**
     * Calculates the distance between two geographical points using the Haversine formula.
     * @param startLat  Latitude of the starting point
     * @param startLng  Longitude of the starting point
     * @param endLat    Latitude of the destination
     * @param endLng    Longitude of the destination
     * @return          Distance in kilometers
     */
    public double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
        final double EARTH_RADIUS_KM = 6371; // Earth radius in kilometers
        double latDistance = Math.toRadians(endLat - startLat);
        double lngDistance = Math.toRadians(endLng - startLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) *
                        Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
