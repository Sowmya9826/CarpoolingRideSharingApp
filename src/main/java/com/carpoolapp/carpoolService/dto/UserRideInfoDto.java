package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class UserRideInfoDto {
    private Long rideId;
    private LocalDate date;
    private String daysOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Location pickupLocation;
    private Location destinationLocation;
    private RideParticipateRole role;

    private List<String> daysOfWeekList;

    public UserRideInfoDto(Long rideId, LocalDate date, String daysOfWeek, LocalTime startTime, LocalTime endTime, Location pickupLocation, Location destinationLocation, RideParticipateRole role) {
        this.rideId = rideId;
        this.date = date;
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.role = role;
    }
}
