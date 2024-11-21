package com.carpoolapp.carpoolService.util;

import java.lang.Math;

public class DistanceCalculator {
    public static double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(endLat - startLat);
        double lonDistance = Math.toRadians(endLng - startLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
