package com.carpoolapp.carpoolService.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RideOwedToUserDto {
    private Long rideId;
    private String pickupLocation;
    private String destinationLocation;
    private LocalDate date;
    private double totalAmountOwed;

    public RideOwedToUserDto(Long rideId, String pickupLocation, String destinationLocation, LocalDate date, double amount) {
        this.rideId = rideId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.date = date;
        this.totalAmountOwed = amount;
    }
}
