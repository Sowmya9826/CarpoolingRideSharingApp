package com.carpoolapp.carpoolService.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FareService {

    private static final double FARE_PER_KM = 1.0;  // Set fare per kilometer (or any other currency unit)

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

    /**
     * Calculates the distance between two geographical points using the Haversine formula.
     * @param startLat  Latitude of the starting point
     * @param startLng  Longitude of the starting point
     * @param endLat    Latitude of the destination
     * @param endLng    Longitude of the destination
     * @return          Distance in kilometers
     */
    private double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
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
