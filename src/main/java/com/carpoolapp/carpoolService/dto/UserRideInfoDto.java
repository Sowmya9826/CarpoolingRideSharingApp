package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UserRideInfoDto {
    private Long rideId;
    private LocalDate date;
    private LocalTime startTime;
    private Location pickupLocation;
    private Location destinationLocation;
    private RideParticipateRole role;

    public UserRideInfoDto(Long rideId, LocalDate date, LocalTime startTime, Location pickupLocation, Location destinationLocation, RideParticipateRole role) {
        this.rideId = rideId;
        this.date = date;
        this.startTime = startTime;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.role = role;
    }
}
