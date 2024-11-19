package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.enums.RideStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MatchingRideDto {
    private Long rideId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private RideStatus status;
    private int availableSeats;
    private String startAddress;
    private double startLatitude;
    private double startLongitude;
    private String endAddress;
    private double endLatitude;
    private double endLongitude;

    public MatchingRideDto(Long rideId, LocalDate date, LocalTime startTime, LocalTime endTime, RideStatus status, int availableSeats, String startAddress, double startLatitude, double startLongitude, String endAddress, double endLatitude, double endLongitude) {
        this.rideId = rideId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.availableSeats = availableSeats;
        this.startAddress = startAddress;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endAddress = endAddress;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
    }

}
